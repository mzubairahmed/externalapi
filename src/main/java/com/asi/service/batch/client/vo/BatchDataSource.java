
package com.asi.service.batch.client.vo;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "Id",
    "Name",
    "Description",
    "TypeCode",
    "BatchId",
    "OrigDataSourceName",
    "AuditStatusCode",
    "ModId",
    "CreateDate"
})
public class BatchDataSource {

    @JsonProperty("Id")
    private Long id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("TypeCode")
    private String typeCode;
    @JsonProperty("BatchId")
    private Long batchId;
    @JsonProperty("OrigDataSourceName")
    private String origDataSourceName;
    @JsonProperty("AuditStatusCode")
    private String auditStatusCode;
    @JsonProperty("ModId")
    private String modId;
    @JsonProperty("CreateDate")
    private String createDate;

    @JsonProperty("Id")
    public Long getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("TypeCode")
    public String getTypeCode() {
        return typeCode;
    }

    @JsonProperty("TypeCode")
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @JsonProperty("BatchId")
    public Long getBatchId() {
        return batchId;
    }

    @JsonProperty("BatchId")
    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    @JsonProperty("OrigDataSourceName")
    public String getOrigDataSourceName() {
        return origDataSourceName;
    }

    @JsonProperty("OrigDataSourceName")
    public void setOrigDataSourceName(String origDataSourceName) {
        this.origDataSourceName = origDataSourceName;
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

    @JsonProperty("CreateDate")
    public String getCreateDate() {
        return createDate;
    }

    @JsonProperty("CreateDate")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
