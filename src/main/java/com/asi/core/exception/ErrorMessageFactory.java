package com.asi.core.exception;

public interface ErrorMessageFactory<T extends Exception> {

	Class<T> getExceptionClass();
	ErrorMessage getErrorMessage(T ex);
	int getResponseCode();
}
