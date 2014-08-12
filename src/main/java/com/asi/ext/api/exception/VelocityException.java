package com.asi.ext.api.exception;

import org.apache.log4j.Logger;

/**
 * VelocityException contains methods for handle all exception which caught while importing/processing products
 * 
 * @author Rahul
 * @version 1.0
 * @see Exception
 */
public class VelocityException extends Exception {

    private final static Logger LOGGER  = Logger.getLogger(VelocityException.class.getName());

    private String              message = null;
    private ExceptionType       exType;

    /**
     * Exception Categories are used to distinguish and priorities the Exceptions
     * there are many categories are defined for velocity application
     * <ul>
     *   <li> CONNECTIVITY 
     *   <li> NULL_POINTER
     *   <li> INVALID_URL
     *   <li> INVALID_JSON
     *   <li> PROPERTY_LOAD_FAILED
     *   <li> URL_NOT_FOUND
     *   <li> UN_UNKOWN
     *   <li> INTERNAL_SERVER_ERROR
     *  <ul>
     *  
     *  @author Rahul
     *  @version 1.0
     *
     */
    public enum ExceptionType {
        CONNECTIVITY, NULL_POINTER, INVALID_URL, INVALID_JSON, PROPERTY_LOAD_FAILED, URL_NOT_FOUND, UN_UNKOWN, INTERNAL_SERVER_ERROR
    }
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 3241522401713179933L;
    /**
     * constructs new VelocityException instance with null message
     * @see java.lang.Exception
     */
    public VelocityException() {
        super();
    }
    /**
     * Default Exception handler
     * @param message to log
     * @param e is the {@linkplain Throwable}
     * @see Exception
     */
    public VelocityException(String message, Throwable e) {
        this.message = message;
    }

    /**
     * constructs new VelocityException with a {@linkplain ExceptionType}, based on {@linkplain ExceptionType}
     * appropriate action/functionality executed
     * @param exType is the Category of this exception
     * @param message to log
     * @param e is the {@linkplain Throwable}
     */
    public VelocityException(String message, ExceptionType exType, Throwable e) {
        this.message = message;
        this.exType = exType;
        if (ExceptionType.CONNECTIVITY.equals(exType)) {
            velocityConnectivityException(message, e);
        } else if (ExceptionType.NULL_POINTER.equals(exType)) {
            velocityNullJsonDataAccessException(message, e);
        } else if (ExceptionType.INVALID_JSON.equals(exType)) {
            velocityInvalidJSON(message, e);
        } else if (ExceptionType.INVALID_URL.equals(exType)) {
            velocityURLNotFoundException(message, e);
        } else if (ExceptionType.PROPERTY_LOAD_FAILED.equals(exType)) {
            velocityPropertyLoadException(message, e);
        } else if (ExceptionType.INVALID_URL.equals(exType)) {
            velocityURLNotFoundException(message, e);
        } else if (ExceptionType.URL_NOT_FOUND.equals(exType)) {
            velocityURLNotFoundException(message, e);
        } else if (ExceptionType.INTERNAL_SERVER_ERROR.equals(exType)) {
            // write code if some special operations needed
        }
    }
    
    /**
     * constructs new VelocityException with a ExceptionType and rootClass (rootClass means the class which received exception)
     * @param message to log
     * @param e is the {@linkplain Throwable}
     * @param exType is the Category of this exception
     * @param rootClass is the class where the exception triggered
     */
    public VelocityException(String message, ExceptionType exType, Throwable e, Class<?> rootClass) {
        this.message = message;
        this.exType = exType;
        if (ExceptionType.CONNECTIVITY.equals(exType)) {
            velocityConnectivityException(message, e, rootClass);
        } else if (ExceptionType.NULL_POINTER.equals(exType)) {
            velocityNullJsonDataAccessException(message, e, rootClass);
        } else if (ExceptionType.INVALID_JSON.equals(exType)) {
            velocityInvalidJSON(message, e, rootClass);
        } else if (ExceptionType.INVALID_URL.equals(exType)) {
            velocityURLNotFoundException(message, e, rootClass);
        } else if (ExceptionType.PROPERTY_LOAD_FAILED.equals(exType)) {
            velocityPropertyLoadException(message, e, rootClass);
        } else if (ExceptionType.INVALID_URL.equals(exType)) {
            velocityURLNotFoundException(message, e, rootClass);
        } else if (ExceptionType.URL_NOT_FOUND.equals(exType)) {
            velocityURLNotFoundException(message, e, rootClass);
        } else if (ExceptionType.INTERNAL_SERVER_ERROR.equals(exType)) {
            // write code if some special operations needed
        }
    }
    
    /** Exception logging 
     * @param message to log
     * @param e is the {@linkplain Throwable}
     * @param rootClass is the Class where the exception triggered
     */
    public VelocityException(String message, Throwable e, Class<?> rootClass) {
        this.message = message;
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);
    }
    /**
     * Handles the exceptions related to Connectivity, HTTP Failures, etc... 
     * @param message is the reason/details
     * @param e is the {@linkplain Throwable}
     */
    private void velocityConnectivityException(String message, Throwable e) {
        LOGGER.error(message);
    }

    private void velocityConnectivityException(String message, Throwable e, Class<?> rootClass) {
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);
    }

    private void velocityNullJsonDataAccessException(String message, Throwable e) {
        LOGGER.error(message);
    }

    private void velocityNullJsonDataAccessException(String message, Throwable e, Class<?> rootClass) {
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);
    }

    private void velocityURLNotFoundException(String message, Throwable e) {
        LOGGER.error(message);
    }

    private void velocityURLNotFoundException(String message, Throwable e, Class<?> rootClass) {
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);

    }

    private void velocityPropertyLoadException(String message, Throwable e) {
        LOGGER.error(message);
    }

    private void velocityPropertyLoadException(String message, Throwable e, Class<?> rootClass) {
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);
    }

    private void velocityInvalidJSON(String message, Throwable e) {
        LOGGER.error(message);
    }

    private void velocityInvalidJSON(String message, Throwable e, Class<?> rootClass) {
        LOGGER.error("Exception occured in " + rootClass != null ? rootClass.getName() : null + ", Message :  " + message);
    }
    
    /**
     * Return the localized message or information of exception
     */
    public String getMessage() {
        return message;
    }

    /**
     * Return the category of the {@linkplain VelocityException}
     * @see ExceptionType
     */
    public ExceptionType getExceptionType() {
        return exType;
    }

}
