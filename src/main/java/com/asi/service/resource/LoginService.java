package com.asi.service.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.service.model.login.Credential;
import com.asi.service.login.client.vo.AccessData;

@RestController
@RequestMapping("Login")
public class LoginService {

    private static Logger _LOGGER = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;
    
    private String loginAPIUrl;

//    @Secured("ROLE_CUSTOMER")
	@RequestMapping(method = RequestMethod.POST, headers = "content-type=application/json, application/xml", produces = {"application/xml", "application/json" })
	public AccessData getAccessToken(HttpEntity<Credential> requestEntity) throws Exception {

    	if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug("Authenticating user credentials data " + requestEntity.getBody().toString());
        }
    	
//    	AccessData response = restTemplate.postForObject("http://stage-espupdates.asicentral.com/api/api/Login", requestEntity.getBody(), AccessData.class);
    	
        AccessData response = restTemplate.postForObject(getLoginAPIUrl(), requestEntity.getBody(), AccessData.class);
        return response;
		
	}

    public String getLoginAPIUrl() {
		return loginAPIUrl;
	}

	public void setLoginAPIUrl(String loginUrl) {
		this.loginAPIUrl = loginUrl;
	}


}
