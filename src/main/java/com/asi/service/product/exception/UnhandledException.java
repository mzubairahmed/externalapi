package com.asi.service.product.exception;

public class UnhandledException extends BaseException {
	private static final long serialVersionUID = -1741803290648794971L;
	public UnhandledException(String productID) {
		super(productID);
		this.errorCode = "error.unhandled.exception.id";
	}
	/**
	 * 
	 */



}