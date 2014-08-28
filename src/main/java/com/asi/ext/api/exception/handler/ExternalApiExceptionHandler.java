/**
 * 
 */
package com.asi.ext.api.exception.handler;

import java.util.List;

import javax.xml.bind.UnmarshalException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * @author Rahul K
 * 
 */
public class ExternalApiExceptionHandler {

    private enum ExceptionTypes {
        UN_RECOGNIZED_FIELD, INVALID_XML_FORMAT, INVALID_JSON, JSON_MAPPING, UNKNOWN
    }

    public static String getMessageFromExceptionForInvalidProductData(Exception e) {
        ExceptionTypes exceptionType = identifyException(e.getCause());
        try {
            return getMessage(e, exceptionType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return e.getLocalizedMessage();
        }

    }

    private static String getMessage(final Exception e, ExceptionTypes exType) {
        StringBuffer buf = new StringBuffer();
        if (exType.equals(ExceptionTypes.UN_RECOGNIZED_FIELD)) {
            buf.append("Request data contains Unrecognized field ");
            buf.append(getUnknowPropertyNameFromException(e.getCause()));
            buf.append(". Please check your data");

            return buf.toString();
        } else if (exType.equals(ExceptionTypes.INVALID_JSON)) {
            buf.append("Invalid JSON :  ");
            buf.append(getMessageForJsonParseException(e.getCause()));
            buf.append(". Please check your data");

            return buf.toString();
        } else if (exType.equals(ExceptionTypes.INVALID_XML_FORMAT)) {
            buf.append("Invalid XML : ");
            buf.append(getMessageFromXmlBindingException(e.getCause()));
            buf.append(". Please check your data");
            return buf.toString();
        } else if (exType.equals(ExceptionTypes.JSON_MAPPING)) {
            buf.append("Invalid JSON : ");
            buf.append(getMessageFromJsonMappingException(e.getCause()));
            buf.append(". Please check your data");
            return buf.toString();
        } else {
            return e.getMessage();
        }
    }

    private static String getUnknowPropertyNameFromException(final Throwable e) {
        if (e instanceof UnrecognizedPropertyException) {
            return ((UnrecognizedPropertyException) e).getUnrecognizedPropertyName();
        } else {
            System.out.println();
            return "unknown";
        }
    }

    private static String getMessageForJsonParseException(final Throwable e) {
        if (e instanceof JsonParseException) {
            return ((JsonParseException) e).getOriginalMessage();
        } else {
            return e.getLocalizedMessage();
        }
    }

    private static String getMessageFromXmlBindingException(final Throwable e) {
        if (e instanceof UnmarshalException) {
            return ((UnmarshalException) e).getLinkedException() != null ? ((UnmarshalException) e).getLinkedException()
                    .getLocalizedMessage() : e.getMessage();
        } else {
            return e.getLocalizedMessage();
        }
    }
    
    public static String getMessageFromJsonMappingException(Throwable e) {
        if (e instanceof JsonMappingException) {
            try {
                List<Reference> paths = ((JsonMappingException) e).getPath();
                StringBuffer buf = new StringBuffer();
                buf.append("Incorrect value nearby ");
                if (paths != null && !paths.isEmpty()) {
                    //buf.append(paths.get(0).getFrom());
                    for (Reference ref : paths) {
                        buf.append(" -> ");
                        buf.append(ref.getFieldName());
                    }
                }
                return buf.toString();
            } catch (Exception ex) {
                return e.getLocalizedMessage();
            }
        } else {
            return e.getLocalizedMessage();
        }
    }

    /**
     * @param throwable
     * @return
     */
    private static ExceptionTypes identifyException(final Throwable throwable) {
        if (throwable instanceof UnrecognizedPropertyException) {
            return ExceptionTypes.UN_RECOGNIZED_FIELD;
        } else if (throwable instanceof JsonParseException) {
            return ExceptionTypes.INVALID_JSON;
        } else if (throwable instanceof UnmarshalException) {
            return ExceptionTypes.INVALID_XML_FORMAT;
        } else if (throwable instanceof JsonMappingException) {
            return ExceptionTypes.JSON_MAPPING;
        } else {
            return ExceptionTypes.UNKNOWN;
        }
    }
}
