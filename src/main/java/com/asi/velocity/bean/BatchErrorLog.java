package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class BatchErrorLog {

    @JsonProperty("Id")
    private Integer id = 0;           // Set ID always as zero for new ErrorLog
    @JsonProperty("BatchId")
    private Integer batchId;
    @JsonProperty("ErrorMessageCode")
    private String  errorCode;
    @JsonProperty("AdditionalInfo")
    private String  additionalInfo;
    @JsonProperty("ExternalProductId")
    private String  externalProductId;

    public BatchErrorLog() {

    }

    public BatchErrorLog(Integer id, Integer batchId, String errorCode, String additionalInfo, String externalProductId) {
        this.id = id;
        this.batchId = batchId;
        this.errorCode = errorCode;
        this.additionalInfo = additionalInfo;
        this.externalProductId = externalProductId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + ((externalProductId == null) ? 0 : externalProductId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BatchErrorLog other = (BatchErrorLog) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null) return false;
        } else if (!additionalInfo.equals(other.additionalInfo)) return false;
        if (batchId == null) {
            if (other.batchId != null) return false;
        } else if (!batchId.equals(other.batchId)) return false;
        if (errorCode == null) {
            if (other.errorCode != null) return false;
        } else if (!errorCode.equals(other.errorCode)) return false;
        if (externalProductId == null) {
            if (other.externalProductId != null) return false;
        } else if (!externalProductId.equals(other.externalProductId)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

}
