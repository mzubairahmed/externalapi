/**
 * 
 */
package com.asi.service.product.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahul K
 * 
 */
public class ExternalApiAuthenticationException extends BaseException {
    /**
     * 
     */
    private static final long serialVersionUID = -285976142619370305L;

    /**
     * @param productID
     */
    public ExternalApiAuthenticationException(String productID) {
        super(productID);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public ExternalApiAuthenticationException(Exception cause, String productId) {
        super(cause, productId);
    }
    
    /**
     * 
     */
    public ExternalApiAuthenticationException(Exception cause, String productId, HttpStatus status) {
        super(cause, productId, status);
    }

}
