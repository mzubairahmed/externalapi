package com.asi.service.product.client.vo;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Batch {
    @JsonProperty("BatchID")
    private Integer              batchId;
    @JsonProperty("BatchTypeCode")
    private String               batchTypeCode;
    @JsonProperty("StartDate")
    private String               startDate;
    @JsonProperty("EndDate")
    private String               endDate;
    @JsonProperty("CompanyId")
    private String               companyId;
    @JsonProperty("Status")
    private String               status           = "Y";
    @JsonProperty("CreatorSignOnId")
    private String               creatorSignOnId;
    // collection
    @JsonProperty("BatchErrorLogs")
    private List<BatchErrorLog>   batchErrorLogs   = new ArrayList<>();
    @JsonProperty("BatchDataSources")
    private List<BatchDataSource> batchDataSources = new ArrayList<>();
    @JsonProperty("BatchProducts")
    private Set<BatchProduct>   batchProducts    = new HashSet<>();

    public Batch() {
    }

    public Batch(Integer batchId, String batchTypeCode, String startDate, String endDate, String companyId, String status,
            List<BatchErrorLog> batchErrorLogs, List<BatchDataSource> batchDataSources) {
        this.batchId = batchId;
        this.batchTypeCode = batchTypeCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companyId = companyId;
        this.status = status;
        this.batchErrorLogs = batchErrorLogs;
        this.batchDataSources = batchDataSources;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchTypeCode() {
        return batchTypeCode;
    }

    public void setBatchTypeCode(String batchTypeCode) {
        this.batchTypeCode = batchTypeCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    /**
     * @return the creatorSignOnId
     */
    public String getCreatorSignOnId() {
        return creatorSignOnId;
    }

    /**
     * @param creatorSignOnId the creatorSignOnId to set
     */
    public void setCreatorSignOnId(String creatorSignOnId) {
        this.creatorSignOnId = creatorSignOnId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BatchErrorLog> getBatchErrorLogs() {
        return batchErrorLogs;
    }

    public void setBatchErrorLogs(List<BatchErrorLog> batchErrorLogs) {
        this.batchErrorLogs = batchErrorLogs;
    }

    public List<BatchDataSource> getBatchDataSources() {
        return batchDataSources;
    }

    public void setBatchDataSources(List<BatchDataSource> batchDataSources) {
        this.batchDataSources = batchDataSources;
    }

    /**
     * @return the batchProducts
     */
    public Set<BatchProduct> getBatchProducts() {
        return batchProducts;
    }

    /**
     * @param batchProducts
     *            the batchProducts to set
     */
    public void setBatchProducts(Set<BatchProduct> batchProducts) {
        this.batchProducts = batchProducts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchDataSources == null) ? 0 : batchDataSources.hashCode());
        result = prime * result + ((batchErrorLogs == null) ? 0 : batchErrorLogs.hashCode());
        result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
        result = prime * result + ((batchProducts == null) ? 0 : batchProducts.hashCode());
        result = prime * result + ((batchTypeCode == null) ? 0 : batchTypeCode.hashCode());
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Batch other = (Batch) obj;
        if (batchDataSources == null) {
            if (other.batchDataSources != null) {
                return false;
            }
        } else if (!batchDataSources.equals(other.batchDataSources)) {
            return false;
        }
        if (batchErrorLogs == null) {
            if (other.batchErrorLogs != null) {
                return false;
            }
        } else if (!batchErrorLogs.equals(other.batchErrorLogs)) {
            return false;
        }
        if (batchId == null) {
            if (other.batchId != null) {
                return false;
            }
        } else if (!batchId.equals(other.batchId)) {
            return false;
        }
        if (batchProducts == null) {
            if (other.batchProducts != null) {
                return false;
            }
        } else if (!batchProducts.equals(other.batchProducts)) {
            return false;
        }
        if (batchTypeCode == null) {
            if (other.batchTypeCode != null) {
                return false;
            }
        } else if (!batchTypeCode.equals(other.batchTypeCode)) {
            return false;
        }
        if (companyId == null) {
            if (other.companyId != null) {
                return false;
            }
        } else if (!companyId.equals(other.companyId)) {
            return false;
        }
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        return true;
    }

}

