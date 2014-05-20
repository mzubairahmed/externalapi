package com.asi.service.core.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;

public class HttpMediaTypeNotSupportedExceptionErrorMessageFactory implements ErrorMessageFactory<HttpMediaTypeNotSupportedException> {
 
    @Override
    public int getResponseCode() {
        return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
    }
 
    @Override
    public Class<HttpMediaTypeNotSupportedException> getExceptionClass() {
        return HttpMediaTypeNotSupportedException.class;
    }
 
    @Override
    public ErrorMessage getErrorMessage(HttpMediaTypeNotSupportedException ex) {
        String unsupported = "Unsupported content type: " + ex.getContentType();
        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        return new ErrorMessage(unsupported, supported);
    }

}
