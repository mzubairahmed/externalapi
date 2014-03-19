package com.asi.service.product.exception;



//@ControllerAdvice
public class ExceptionControllerAdvice {

	//@ExceptionHandler(Exception.class)
    public String exception(Exception e) {
		return e.getLocalizedMessage();
	}
}
