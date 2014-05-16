package com.asi.service.product.exception;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.http.HttpStatus;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/error" , name="errorInfo")
@XmlType(propOrder={"errorMessage","errorURL","statusCode"})
public class ErrorInfo {

	private String errorMessage=null;
	private String errorURL;
	private HttpStatus statusCode;

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorURL() {
		return errorURL;
	}
	public void setErrorURL(String errorURL) {
		this.errorURL = errorURL;
	}
	public HttpStatus getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}
	
}
