package com.asi.service.product.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String productID="";
	private String url;
	protected HttpStatus statusCode;
	protected String errorCode;
	
	public BaseException(String productID)
	{
		
		this.productID = productID;
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
	
}
