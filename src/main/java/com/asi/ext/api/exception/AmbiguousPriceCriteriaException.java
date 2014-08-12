/**
 * 
 */
package com.asi.ext.api.exception;

/**
 * @author Rahul K
 * 
 */
public class AmbiguousPriceCriteriaException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1496895169820826404L;

    /**
     * 
     */
    public AmbiguousPriceCriteriaException() {

    }

    /**
     * @param message
     */
    public AmbiguousPriceCriteriaException(String message) {
        super(message);

    }

    /**
     * @param cause
     */
    public AmbiguousPriceCriteriaException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public AmbiguousPriceCriteriaException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public AmbiguousPriceCriteriaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
