package com.asi.core.exception;

import com.asi.service.product.exception.BaseException;

public class ResponseNotValidException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ResponseNotValidException(String productID) {
		super(productID);
		this.errorCode = "error.notvalid.response.id";
	}
}
