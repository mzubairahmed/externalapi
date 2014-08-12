package com.asi.ext.api.radar.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class BatchProduct {
    @JsonProperty("BatchProductId")
    private String batchProductId;
    @JsonProperty("BatchId")
    private String batchId;
    @JsonProperty("ExternalProductId")
    private String externalProductId;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("BatchStatusCode")
    private String batchStatusCode;
    @JsonProperty("CreateDate")
    private String createDate;

    /**
     * @return the batchProductId
     */
    public String getBatchProductId() {
        return batchProductId;
    }

    /**
     * @param batchProductId
     *            the batchProductId to set
     */
    public void setBatchProductId(String batchProductId) {
        this.batchProductId = batchProductId;
    }

    /**
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * @param batchId
     *            the batchId to set
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * @return the externalProductId
     */
    public String getExternalProductId() {
        return externalProductId;
    }

    /**
     * @param externalProductId
     *            the externalProductId to set
     */
    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the batchStatusCode
     */
    public String getBatchStatusCode() {
        return batchStatusCode;
    }

    /**
     * @param batchStatusCode
     *            the batchStatusCode to set
     */
    public void setBatchStatusCode(String batchStatusCode) {
        this.batchStatusCode = batchStatusCode;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
