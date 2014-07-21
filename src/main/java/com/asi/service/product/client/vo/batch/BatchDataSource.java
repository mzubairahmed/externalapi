package com.asi.service.product.client.vo.batch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BatchDataSource {

    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private String  name;
    @JsonProperty("Description")
    private String  description;
    @JsonProperty("TypeCode")
    private String  typeCode;
    @JsonProperty("BatchId")
    private Integer batchId;
    @JsonProperty("CreateDate")
    private String  createDate;

    public BatchDataSource() {

    }

    public BatchDataSource(Integer id, String name, String description, String typeCode, Integer batchId, String createDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeCode = typeCode;
        this.batchId = batchId;
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((typeCode == null) ? 0 : typeCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BatchDataSource other = (BatchDataSource) obj;
        if (batchId == null) {
            if (other.batchId != null) return false;
        } else if (!batchId.equals(other.batchId)) return false;
        if (createDate == null) {
            if (other.createDate != null) return false;
        } else if (!createDate.equals(other.createDate)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (typeCode == null) {
            if (other.typeCode != null) return false;
        } else if (!typeCode.equals(other.typeCode)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "BatchDataSource [id=" + id + ", name=" + name + ", description=" + description + ", typeCode=" + typeCode
                + ", batchId=" + batchId + ", createDate=" + createDate + "]";
    }
}
