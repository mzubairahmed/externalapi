
package com.asi.service.batch.client.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BatchId",
    "BatchTypeCode",
    "StartDate",
    "Status",
    "CompanyId",
    "CreatorSignOnId",
    "BatchDataSources",
    "BatchErrorLogs",
    "BatchProducts",
    "AuditStatusCode",
    "ModId",
    "SignOnId",
    "CreateDate"
})
public class Batch {

    @JsonProperty("BatchId")
    private Long batchId;
    @JsonProperty("BatchTypeCode")
    private String batchTypeCode;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("CompanyId")
    private Long companyId;
    @JsonProperty("CreatorSignOnId")
    private Long creatorSignOnId;
    @JsonProperty("BatchDataSources")
    private List<BatchDataSource> batchDataSources = new ArrayList<BatchDataSource>();
    @JsonProperty("BatchErrorLogs")
    private List<Object> batchErrorLogs = new ArrayList<Object>();
    @JsonProperty("BatchProducts")
    private List<Object> batchProducts = new ArrayList<Object>();
    @JsonProperty("AuditStatusCode")
    private String auditStatusCode;
    @JsonProperty("ModId")
    private String modId;
    @JsonProperty("SignOnId")
    private Long signOnId;
    @JsonProperty("CreateDate")
    private String createDate;

    @JsonProperty("BatchId")
    public Long getBatchId() {
        return batchId;
    }

    @JsonProperty("BatchId")
    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    @JsonProperty("BatchTypeCode")
    public String getBatchTypeCode() {
        return batchTypeCode;
    }

    @JsonProperty("BatchTypeCode")
    public void setBatchTypeCode(String batchTypeCode) {
        this.batchTypeCode = batchTypeCode;
    }

    @JsonProperty("StartDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("CompanyId")
    public Long getCompanyId() {
        return companyId;
    }

    @JsonProperty("CompanyId")
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @JsonProperty("CreatorSignOnId")
    public Long getCreatorSignOnId() {
        return creatorSignOnId;
    }

    @JsonProperty("CreatorSignOnId")
    public void setCreatorSignOnId(Long creatorSignOnId) {
        this.creatorSignOnId = creatorSignOnId;
    }

    @JsonProperty("BatchDataSources")
    public List<BatchDataSource> getBatchDataSources() {
        return batchDataSources;
    }

    @JsonProperty("BatchDataSources")
    public void setBatchDataSources(List<BatchDataSource> batchDataSources) {
        this.batchDataSources = batchDataSources;
    }

    @JsonProperty("BatchErrorLogs")
    public List<Object> getBatchErrorLogs() {
        return batchErrorLogs;
    }

    @JsonProperty("BatchErrorLogs")
    public void setBatchErrorLogs(List<Object> batchErrorLogs) {
        this.batchErrorLogs = batchErrorLogs;
    }

    @JsonProperty("BatchProducts")
    public List<Object> getBatchProducts() {
        return batchProducts;
    }

    @JsonProperty("BatchProducts")
    public void setBatchProducts(List<Object> batchProducts) {
        this.batchProducts = batchProducts;
    }

    @JsonProperty("AuditStatusCode")
    public String getAuditStatusCode() {
        return auditStatusCode;
    }

    @JsonProperty("AuditStatusCode")
    public void setAuditStatusCode(String auditStatusCode) {
        this.auditStatusCode = auditStatusCode;
    }

    @JsonProperty("ModId")
    public String getModId() {
        return modId;
    }

    @JsonProperty("ModId")
    public void setModId(String modId) {
        this.modId = modId;
    }

    @JsonProperty("SignOnId")
    public Long getSignOnId() {
        return signOnId;
    }

    @JsonProperty("SignOnId")
    public void setSignOnId(Long signOnId) {
        this.signOnId = signOnId;
    }

    @JsonProperty("CreateDate")
    public String getCreateDate() {
        return createDate;
    }

    @JsonProperty("CreateDate")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
