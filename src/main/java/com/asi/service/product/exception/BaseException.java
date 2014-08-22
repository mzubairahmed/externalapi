package com.asi.service.product.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String            productID        = "";
    private String            url;
    protected HttpStatus      statusCode;
    protected String          errorCode;
    private String            message;

    public BaseException(String productID) {

        this.productID = productID;
    }

    public BaseException(Exception e, String productID) {
        super(e);
        this.productID = productID;
    }

    public BaseException(Exception e, String productID, HttpStatus status) {
        super(e);
        this.productID = productID;
        this.statusCode = status;
    }

    public BaseException(String xid, String message) {
        this.productID = xid;
        this.message = message;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    

}
