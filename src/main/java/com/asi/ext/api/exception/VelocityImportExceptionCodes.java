/**
 * 
 */
package com.asi.ext.api.exception;

/**
 * @author Rahul K
 * 
 */
public final class VelocityImportExceptionCodes {

    // PriceGrid parsing related exceptions
    public static final String  PG_EXCEPTION_UNKOWN            = "VIMPG00UNK";
    // Null Pointer Exception, occurred in Internal Processing
    public static final String  NULL_EXCEPTION                 = "VIMNPTR001";
    // Invalid Product Posted API returned a 400 response code
    public static final String  INVALID_PRODUCT_400            = "VIMINVPRD400";
    // Internal Server Error - Status code 500
    public static final String  INTERNAL_SERVER_ERROR_500      = "VIMINTSER500";
    // Requested HTTP Resource not found / forbidden access - 404/403
    public static final String  HTTP_RESOURCE_NOT_FOUND        = "VIMHPRNTF404";
    // If any required fields are missing or if any of the fields in model contains in valid data API will throw this error
    public static final String  HTTP_INVALID_PRD_FIELD_MISSING = "VIMINPFLDINLD";
    public static final String  HTTP_INVALID_PRD_FIELD_ISSUES  = "VIM_INVALID_MOD_ISSUE";

    // Possible error messages from API
    public static final String PRODUCT_NOT_ACTIVE             = "{\"Message\":\"Not Valid\"}";
    public static final String PRODUCT_NOT_VALID_ERR          = "{\"Errors\"";
    public static final String PRODUCT_VALIDATION_FAILED      = "{\"Message\":";
    public static final String PRODUCT_MODEL_FIELD_MISSING    = "{\"model";
    public static final String PRODUCT_MODEL_FIELD_ISSUES     = "[{\"msg\"";

    /**
     * Checks for the exception is public or private
     * 
     * @param message
     * @return
     */
    public static boolean isLoggableError(String message) {

        message = message.trim();
        if (PRODUCT_NOT_ACTIVE.equalsIgnoreCase(message)) {
            return false;
        } else if (String.valueOf(message).startsWith(PRODUCT_NOT_VALID_ERR)) {
            // Mule don't need log this kind of errors, API already logged
            return false;
        } else if (String.valueOf(message).startsWith(PRODUCT_MODEL_FIELD_MISSING)) {
            return true;
        } else if (String.valueOf(message).startsWith(PRODUCT_VALIDATION_FAILED)) {
            return true;
        } else {
            return false;
        }
    }
}
