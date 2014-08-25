/**
 * 
 */
package com.asi.service.product.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahul K
 *
 */
public class InvalidProductException extends BaseException {

    /**
     * @param e
     * @param productID
     * @param status
     */
    public InvalidProductException(Exception e, String productID, HttpStatus status) {
        super(e, productID, status);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1300399632728954966L;
    
    public InvalidProductException(String xid, String message) {
        super(xid, message);
    }

    
}
