package com.asi.service.product.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1741803290648794971L;
	private  String productID="";
	private HttpStatus statusCode;
	public ProductNotFoundException(String productID)
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

	public ProductNotFoundException(String message, Throwable throwable) {
		super(throwable);
	}

}
