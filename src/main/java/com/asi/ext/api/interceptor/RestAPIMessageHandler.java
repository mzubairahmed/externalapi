package com.asi.ext.api.interceptor;

/**
 * 
 */
import java.util.Set;

import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityImportExceptionCodes;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

/**
 * @author Rahul K
 * 
 */
@SuppressWarnings("unused")
public final class RestAPIMessageHandler implements java.io.Serializable {

    /**
     * 
     */
    private static final Logger                LOGGER                 = Logger.getLogger(RestAPIMessageHandler.class);
    private static final long                  serialVersionUID       = -91361236961946629L;

    private static final RestAPIMessageHandler velocityMessageHandler = new RestAPIMessageHandler();

    private enum VelocityMessageCodes {
        PROD_INVALID, PROD_FIELD_MISSING, PROD_VALIDATION, PROD_ERR, PROD_UNK
    }

    // Disable creation of new object
    private RestAPIMessageHandler() {
    }

    public static RestAPIMessageHandler getInstance() {
        return velocityMessageHandler;
    }

    private boolean isLoggableException(VelocityMessageCodes code) {
        if (code.equals(VelocityMessageCodes.PROD_VALIDATION) || code.equals(VelocityMessageCodes.PROD_FIELD_MISSING)) {
            return true;
        } else {
            return false;
        }
    }

    private VelocityMessageCodes identifyException(String message) {

        if (message.startsWith(VelocityImportExceptionCodes.PRODUCT_NOT_ACTIVE)) {
            return VelocityMessageCodes.PROD_INVALID;
        } else if (message.startsWith(VelocityImportExceptionCodes.PRODUCT_NOT_VALID_ERR)) {
            return VelocityMessageCodes.PROD_ERR;
        } else if (message.startsWith(VelocityImportExceptionCodes.PRODUCT_MODEL_FIELD_MISSING)) {
            return VelocityMessageCodes.PROD_FIELD_MISSING;
        } else if (message.startsWith(VelocityImportExceptionCodes.PRODUCT_VALIDATION_FAILED)) {
            return VelocityMessageCodes.PROD_VALIDATION;
        } else {
            return VelocityMessageCodes.PROD_UNK;
        }
    }

    private boolean registerException(String batchId, String xid, final Set<String> messages) {
        String finalMessage = null;
        for (String s : messages) {
            ProductDataStore.addUnhandledErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_GENERIC_ERROR, s);
        }
        return !messages.isEmpty();
    }

    private Set<String> processErrorMessage(String message) {
        VelocityMessageCodes code = identifyException(message);
        if (isLoggableException(code)) {
            if (code == VelocityMessageCodes.PROD_FIELD_MISSING) {
                // Message will be in a JSON key/value pair
                try {
                    return JsonProcessor.getErrorMessageFromJson(message);
                } catch (Exception e) {
                    LOGGER.error("Some error occured while processing error message", e);
                }
            }
        } else {
            LOGGER.info("The message returned by API is not Loggable to Batch");
        }

        return null;
    }

    private boolean startErrorLogging(String batchId, String externalProductId, String message) {
        Set<String> finalMessages = processErrorMessage(message);

        if (finalMessages != null && !finalMessages.isEmpty()) {
            return registerException(batchId, externalProductId, finalMessages);
        } else {
            return false;
        }
    }

    /**
     * Log Error to Batch collection. This method will evaluate the message and if the message requires batch logging then will log
     * to batch otherwise not
     * 
     * @param externalProductId
     *            is the ExternalId of Product
     * @param message
     *            received from API or External resource
     * @return true if message is logged to Batch otherwise false
     */
    public boolean handleMessage(String batchId, String externalProductId, String message) {
        if (CommonUtilities.isValueNull(batchId)) {
            LOGGER.info("There is no BatchID found for this message, without batchId we can't log a error");
            return false;
        } else if (CommonUtilities.isValueNull(externalProductId)) {
            LOGGER.info("There is no XID found for this message, without XID we can't log a error");
            return false;
        }
        return startErrorLogging(batchId, externalProductId, message);
    }
}