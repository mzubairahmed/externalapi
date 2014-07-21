package com.asi.ext.api.product.transformers;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.asi.ext.api.radar.model.Batch;
import com.asi.ext.api.radar.model.BatchErrorLog;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.rest.JerseyClientPut;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.RestAPIProperties;

/**
 * BatchProcessor contains functions and methods related to processing batch log processing like
 * creating a Batch Record and Updating a Batch record. 
 *  
 * @author Shravan, Murali, Rahul
 * @version 1.5
 * 
 *
 */
public class BatchProcessor {

    private final static Logger LOGGER          = Logger.getLogger(BatchProcessor.class);
    
    public void processErrorLogs() {
    }
    
    /**
     * Sends a Batch update request to <code>{@linkplain JerseyClientPut}</code>
     * @param batch contains the data need to be updated
     * @param batchId Id of the Batch we need to update
     * @return <code>true</code> if Error log successfully send to batch API otherwise <code>false</code>
     */
    public boolean sendErrorLogToAPI(Batch batch, String batchId) {

        // Need to organize code
        String batchJSON = JsonProcessor.convertBeanToJson(batch);

        if (batchJSON != null) {
            JerseyClientPut jerseyClientPut = new JerseyClientPut();
            String response = jerseyClientPut.doPutRequest(RestAPIProperties.get(ApplicationConstants.BATCH_PROCESSING_FINALIZE), batchJSON);
            if (response != null) {
                // error log send to velocity server
                return true;
            } else {
                // error
                LOGGER.info(response);
            }
        } else {
            // batch com.asi.util.json cannot be null
            LOGGER.info("Batch JSON cannot be " + batchJSON);
        }
        return false;

    }

    /**
     * Convert given batchErrorLog string to a {@linkplain BatchErrorLog} and Store it into 
     * a new {@linkplain Batch} object which derived from oldBatch
     * @param batchErrorlogs is concatenated batch logs, the delimter in batchErrorLog string is $ symobl
     * @param oldbatch is the Batch data which we created in {@linkplain SplitterTransformer}
     * @return updated {@linkplain Batch} record
     */
    public Batch converErrorLogsToBatchData(String batchErrorlogs, Batch oldbatch) {
        // First check batchId is not null or is not empty
        if (oldbatch != null && oldbatch.getBatchId() != null) {
            Batch batch = new Batch();
            // Set basic props to Batch
            batch.setBatchId(oldbatch.getBatchId());
            batch.setBatchTypeCode(oldbatch.getBatchTypeCode());
            if (oldbatch.getStartDate() != null && !oldbatch.getStartDate().trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                batch.setStartDate(oldbatch.getStartDate());
            } else {
                batch.setStartDate(new Timestamp(System.currentTimeMillis()).toString());
            }
            if (oldbatch.getEndDate() != null && !oldbatch.getEndDate().trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                batch.setEndDate(oldbatch.getEndDate());
            } else if (oldbatch.getStartDate() != null && !oldbatch.getStartDate().trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)){
                batch.setEndDate(oldbatch.getStartDate());
            } else {
                batch.setEndDate(new Timestamp(System.currentTimeMillis()).toString());
            }
            
            // Set BatchDatasources
            batch.setBatchDataSources(oldbatch.getBatchDataSources());

            String[] batchErrorArray = batchErrorlogs.split("\\$");
            if (batchErrorArray != null && batchErrorArray.length != 0) {
                for (String errorGrp : batchErrorArray) { // each element should be like "error:ERR_CODE:The error message" .
                    if (errorGrp != null && !errorGrp.isEmpty()) {
                        String batchErrorKeyValueArray[] = errorGrp.split(":");
                        // This array will contain structure like [0]th element in
                        // this array is type or ExternalID and [1]th element is ERROR CODE and [2] element is the message
                        BatchErrorLog err = getErrorLog(batchErrorKeyValueArray[0].replace("$", ""), batchErrorKeyValueArray[1],
                                batchErrorKeyValueArray[2], String.valueOf(oldbatch.getBatchId()));
                        if (err != null) {
                            batch.getBatchErrorLogs().add(err);
                        }
                    }
                }
            } else {
                // BatchErrorLog is empty
                return batch;
            }
            return batch;
        }
        return null;
    }
    
    /**
     * Process and categories each batch error log entry
     * @param grp is the category of error
     * @param errorCode is the error type
     * @param message is the additional information about the error
     * @param batchId is the parent batch for this log entry
     * @return new {@linkplain BatchErrorLog}
     */
    private BatchErrorLog getErrorLog(String grp, String errorCode, String message, String batchId) {

        BatchErrorLog errorLog = new BatchErrorLog();

        errorLog.setBatchId(Integer.parseInt(batchId));
        errorLog.setErrorCode(errorCode);
        if (grp.startsWith("Ext-")) {
            errorLog.setAdditionalInfo(message);
            String[] extProdArry = grp.split("Ext-");
            if (extProdArry != null && extProdArry.length > 0)
                errorLog.setExternalProductId(extProdArry[1]);
            else
                errorLog.setExternalProductId(null);
        } else {
            errorLog.setAdditionalInfo(grp.toUpperCase() + " : " + message);
        }

        return errorLog;
    }
    
    /**
     * Creates new batch record for each Import with the companyId and default values
     * @param companyId is the Company which the products belongs
     * @return new Batch record or null
     * @throws VelocityException
     */
    /*public Batch createBatchRecord(String companyId) throws VelocityException {
        if (companyId != null && !companyId.isEmpty() && !companyId.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
            // Set batch record properties for new batch creation
            Batch newBatchRecord = new Batch();
            newBatchRecord.setBatchId(0);
            newBatchRecord.setCompanyId(companyId);
            newBatchRecord.setBatchTypeCode(ApplicationConstants.CONST_BATCH_TYPE_CODE_IMPRT);
            newBatchRecord.setStatus("N");
            newBatchRecord.setStartDate(new Timestamp(System.currentTimeMillis()).toString());
            newBatchRecord.setEndDate(new Timestamp(System.currentTimeMillis()).toString());
            // Create new BatchDataSource
            BatchDataSource newBatchDataSource = new BatchDataSource();
            newBatchDataSource.setBatchId(0);
            newBatchDataSource.setCreateDate(new Timestamp(System.currentTimeMillis()).toString());
            newBatchDataSource.setTypeCode(ApplicationConstants.CONST_BATCH_TYPE_CODE_IMPRT);
            newBatchDataSource.setId(0);
            newBatchDataSource.setDescription("Mule Import Controller");
            // Add new BatchDataSource to Batch
            newBatchRecord.getBatchDataSources().add(newBatchDataSource);

            // Now convert Batch record to JSON for sending to POST method (creation of new Batch)

            String batchJson = JsonProcessor.convertBeanToJson(newBatchRecord);

            JerseyClientPost jersyClient = new JerseyClientPost();
            // Send converted batchJson to JerseyClient POST
            String result = jersyClient.doPostRequest(RestAPIProperties.get(ApplicationConstants.BATCH_PROCESSING), batchJson);
            // Once result return parse it back to Batch bean if its not null
            if (result != null && !result.isEmpty()) {
                newBatchRecord = JsonProcessor.convertJsonToBean(result, Batch.class);
                return newBatchRecord;
            } else {
                throw new VelocityException("Batch Record creation failed, returned null value", null);
            }
        } else {
            return null;
        }
    }*/
}
