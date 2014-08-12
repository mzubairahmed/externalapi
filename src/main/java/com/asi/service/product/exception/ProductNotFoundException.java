package com.asi.service.product.exception;


public class ProductNotFoundException extends BaseException {
	private static final long serialVersionUID = -1741803290648794971L;
	public ProductNotFoundException(String productID) {
		super(productID);
		this.errorCode = "error.no.priduct.id";
	}
	/**
	 * 
	 */
	public ProductNotFoundException(Exception cause, String productId) {
		super(cause, productId);
	}


}
