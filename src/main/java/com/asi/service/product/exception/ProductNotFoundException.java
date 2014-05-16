package com.asi.service.product.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends IOException {

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

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
