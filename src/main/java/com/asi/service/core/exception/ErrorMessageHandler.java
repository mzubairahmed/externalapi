package com.asi.service.core.exception;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.asi.service.product.exception.BaseException;
@Component
public final class ErrorMessageHandler {
	@Autowired
	private  MessageSource messageSource;
	public  ErrorMessage prepairError(BaseException exception,  HttpServletRequest request,List<String> errorsList,HttpStatus status)
	{
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage(exception.getErrorCode(), null, locale);
    	errorMessage += " " + exception.getProductID();
        String errorURL = request.getRequestURL().toString();
        ErrorMessage errorInfo = new ErrorMessage();
		errorInfo.setErrorMessage(errorMessage);
		errorInfo.setErrorURL(errorURL);
		errorInfo.setStatusCode(status);
		errorInfo.setErrors(errorsList);
		return errorInfo;
	}
	public ErrorMessage prepairError(String errorKey, HttpServletRequest request, List<String> errorsList, HttpStatus status)
	{
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage(errorKey, null, locale);
		ErrorMessage errorInfo = new ErrorMessage();
		String errorURL = request.getRequestURL().toString();
		errorInfo.setErrorMessage(errorMessage);
		errorInfo.setErrorURL(errorURL);
		errorInfo.setStatusCode(status);
		errorInfo.setErrors(errorsList);
		return errorInfo;
	}
}
