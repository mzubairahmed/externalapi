package com.asi.service.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.ext.api.service.LoginService;
import com.asi.ext.api.service.model.LoginError;
import com.asi.ext.api.service.model.login.Credential;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.login.client.vo.AccessData;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

@RestController
@RequestMapping("Login")
public class LoginServiceResource {

    private static Logger _LOGGER = LoggerFactory.getLogger(LoginServiceResource.class);

    @Autowired
    private LoginService  loginService;

    @RequestMapping(method = RequestMethod.POST, headers = "content-type=application/json, application/xml", produces = {
            "application/xml", "application/json" })
    public ResponseEntity<?> getAccessToken(HttpEntity<Credential> requestEntity) throws Exception {

        if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug("Authenticating user credentials data " + requestEntity.getBody().toString());
        }

        AccessData response = null;
        
        try {
            response = loginService.getAuthToken(requestEntity.getBody());
            if (response != null && !CommonUtilities.isValueNull(response.getAccessToken())) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (ExternalApiAuthenticationException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return new ResponseEntity<>(new LoginError("Unauthorized"), e.getStatusCode());
            } else if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return new ResponseEntity<>(new LoginError(e.getMessage()), e.getStatusCode());
            } else if (e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return new ResponseEntity<>(new LoginError("Failed to Process request"), e.getStatusCode());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new LoginError("Failed to Process request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}
