package com.asi.service.core.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/error" , name="errorInfo")
@XmlType(propOrder={"errorMessage","errorURL","statusCode","errors","errorDate","remoteHost"})
@JsonPropertyOrder({"errorMessage","errorURL","statusCode","errors","errorDate","remoteHost"})
public class ErrorMessage {
	private String errorMessage=null;
	private String errorURL;
	private HttpStatus statusCode;
	private Date errorDate;
	private String remoteHost;
	
	

    public Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

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

	private List<String> errors;
    
    public ErrorMessage() {
    }
 
    public ErrorMessage(List<String> errors) {
        this.errors = errors;
    }
 
    public ErrorMessage(String error) {
        this(Collections.singletonList(error));
    }
 
    public ErrorMessage(String ... errors) {
        this(Arrays.asList(errors));
    }
 
    public List<String> getErrors() {
        return errors;
    }
 
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
