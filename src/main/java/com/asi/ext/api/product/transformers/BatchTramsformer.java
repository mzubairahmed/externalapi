package com.asi.ext.api.product.transformers;

import java.sql.Timestamp;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.radar.model.Batch;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.RestAPIProperties;

/**
 * BatchTransformer class is responsible for logging batch errors to Batch API and
 * 
 * @see AbstractMessageTransformer
 * 
 */

public class BatchTramsformer {
    private final static Logger LOGGER           = Logger.getLogger(BatchTramsformer.class);

    private BatchProcessor      batchProcessor   = new BatchProcessor();
    private JerseyClientPost    jerseyClientPost = new JerseyClientPost();

    /**
     * Functionality of {@link #transformMessage(MuleMessage, String)} in BatchTransformer to process/handle errors occurred
     * while importing products. While processing a {@linkplain Product} there will be some chances for some required fields were
     * missing at that time
     * we need to log this information to the BatchAPI. We store the errors in a mule session property
     * 
     * @param muleMessage
     *            which contains the batch error logs and Batch data which is created {@linkplain SplitterTransformer},
     *            batchErrors log can get from the Mule Session property
     * @param arg1
     *            is the outputEncoding.
     **/
    public Object transformMessage(Object muleMessage, String arg1) throws TransformerException {

        String batchData = "";//muleMessage.getProperty("batchData", PropertyScope.SESSION);
        String batchErrorLogs = "";//muleMessage.getProperty("batchErrorLog", PropertyScope.SESSION);
        String totalNumberOfRecord = "";// String.valueOf(muleMessage.getProperty("totalProductCount", PropertyScope.SESSION));
        String numberOfRecordFailed = "";// String.valueOf(muleMessage.getProperty("recordFailedToProcess", PropertyScope.SESSION));
        LOGGER.info("RESULT : " + numberOfRecordFailed + " product(s) where failed to process out of " + totalNumberOfRecord);

        String internalServerErrors = "";//muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION);

        if (internalServerErrors != null && !internalServerErrors.isEmpty() && !internalServerErrors.equalsIgnoreCase("null")) {
            int internalServerErrorCount = 0;
            int otherErrorCount = 0;
            String[] errors = null;
            if ((errors = internalServerErrors.split("\\$")) != null && errors.length > 0) {
                for (String error : errors) {
                    String[] errorTokens = error.split(":");
                    if (errorTokens != null && errorTokens.length == 3 && error.contains("ERR-CODE-500")) {
                        LOGGER.info(errorTokens[0] + ", External Product ID : " + errorTokens[1] + " , Error Message : "
                                + errorTokens[2]);
                        internalServerErrorCount++;
                    } else if (errorTokens != null && errorTokens.length == 2) {
                        LOGGER.info(errorTokens[0] + ", Message : " + errorTokens[1]);
                        otherErrorCount++;
                    }
                }
            }
            LOGGER.info("Total InternalServerException(Status Code : 500) Occured " + internalServerErrorCount);
            LOGGER.info("Total Other HttpException/ConnectivityErrors Occured " + otherErrorCount);
        }

        LOGGER.info(" BatchData which we recived from GET request : " + batchData);
        LOGGER.info(" Errors Captured : " + batchErrorLogs);

        Batch oldBatch = JsonProcessor.convertJsonToBean(batchData, Batch.class);
        try {
            String updatedBatchResponse = jerseyClientPost.getLookupsResponse(RestAPIProperties
                    .get(ApplicationConstants.BATCH_PROCESSING) + "/" + oldBatch.getBatchId());
            oldBatch = JsonProcessor.convertJsonToBean(updatedBatchResponse, Batch.class);
        } catch (VelocityException e1) {
            LOGGER.error("Exception while reading batch data for batch " + batchData, e1);
        }

        if (batchErrorLogs != null && !batchErrorLogs.trim().isEmpty()) {
            Batch batch = batchProcessor.converErrorLogsToBatchData(batchErrorLogs, oldBatch);

            if (batch != null) {
                batch.setEndDate(new Timestamp(System.currentTimeMillis()).toString());
                // batch.setStatus("S");
                LOGGER.info("Batch Log Send Request : "
                        + batchProcessor.sendErrorLogToAPI(batch, String.valueOf(batch.getBatchId())));
            } else {
                LOGGER.error("Unable to process BatchLogErrors");
            }
        } else {
            LOGGER.info("There is no error for this import, finalizing batch");
            oldBatch.setStatus("S");
            oldBatch.setEndDate(new Timestamp(System.currentTimeMillis()).toString());
            LOGGER.info("Batch Log Send Request : "
                    + batchProcessor.sendErrorLogToAPI(oldBatch, String.valueOf(oldBatch.getBatchId())));
        }

        try {
            //LOGGER.info("PayLoad : " + muleMessage.getPayloadAsString());
        } catch (Exception e) {
            LOGGER.info("Exception Occured:" + e.getMessage());
        }
        return muleMessage;
    }
}
