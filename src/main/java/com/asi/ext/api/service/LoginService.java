/**
 * 
 */
package com.asi.ext.api.service;

import com.asi.ext.api.service.model.login.Credential;
import com.asi.service.login.client.vo.AccessData;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

/**
 * @author Rahul K
 * 
 */
public interface LoginService {

    public AccessData getAuthToken(Credential credential) throws ExternalApiAuthenticationException;
}
