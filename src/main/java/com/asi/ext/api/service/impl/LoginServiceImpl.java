/**
 * 
 */
package com.asi.ext.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.service.LoginService;
import com.asi.ext.api.service.model.login.Credential;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.login.client.vo.AccessData;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

/**
 * @author Rahul K
 * 
 */
public class LoginServiceImpl implements LoginService {

    private final static Logger _LOGGER = Logger.getLogger(LoginService.class.getName());

    private RestTemplate        restTemplate;
    private String              loginAPIUrl;

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.service.LoginService#getAuthToken(com.asi.ext.api.service.model.login.Credential)
     */
    @Override
    public AccessData getAuthToken(Credential credential) throws ExternalApiAuthenticationException {
        _LOGGER.info("Validating user credentials");

        if (isValidForAuthToken(credential)) {
            return doLogin(credential);
        }
        _LOGGER.info("Completed user credentials validation");

        return null;
    }

    private AccessData doLogin(Credential cred) throws ExternalApiAuthenticationException {
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("Connecting to Radar Login Endpoint");
            _LOGGER.trace("Login Data : " + cred);
        }
        AccessData accessData = null;
        try {
            accessData = restTemplate.postForObject(loginAPIUrl, cred, AccessData.class);
        } catch (HttpClientErrorException hce) {
            if (hce.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new ExternalApiAuthenticationException(hce, null, HttpStatus.UNAUTHORIZED);
            } else if (hce.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                ExternalApiAuthenticationException exception = new ExternalApiAuthenticationException(hce, null,
                        HttpStatus.BAD_REQUEST);
                exception.setMessage("Bad Request");
                throw exception;
            } else if (hce.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                throw new ExternalApiAuthenticationException(hce, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            throw new ExternalApiAuthenticationException(e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("Completed LoginRequest to Radar");
            _LOGGER.trace("Login AuthToken : " + accessData);
        }
        return accessData;
    }

    private boolean isValidForAuthToken(Credential cred) throws ExternalApiAuthenticationException {
        ExternalApiAuthenticationException exception = null;
        if (cred == null || cred.equals(new Credential())) {
            exception = new ExternalApiAuthenticationException(null);
            exception.setMessage("ASI Number, Username and Password is required");
        } else if (CommonUtilities.isValueNull(cred.getAsi()) && CommonUtilities.isValueNull(cred.getUsername())
                && CommonUtilities.isValueNull(cred.getPassword())) {
            exception = new ExternalApiAuthenticationException(null);
            exception.setMessage("ASI Number, Username and Password is required");
        } else if (CommonUtilities.isValueNull(cred.getAsi())) {
            exception = new ExternalApiAuthenticationException(null);
            exception.setMessage("ASI Number is required");
        } else if (CommonUtilities.isValueNull(cred.getUsername())) {
            exception = new ExternalApiAuthenticationException(null);
            exception.setMessage("Username is required");
        } else if (CommonUtilities.isValueNull(cred.getPassword())) {
            exception = new ExternalApiAuthenticationException(null);
            exception.setMessage("Password is required");
        }
        if (exception == null) {
            return true;
        } else {
            exception.setStatusCode(HttpStatus.BAD_REQUEST);
            throw exception;
        }
    }

    /**
     * @param restTemplate
     *            the restTemplate to set
     */
    @Required
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param loginAPIUrl
     *            the loginAPIUrl to set
     */
    @Required
    public void setLoginAPIUrl(String loginAPIUrl) {
        this.loginAPIUrl = loginAPIUrl;
    }

}
