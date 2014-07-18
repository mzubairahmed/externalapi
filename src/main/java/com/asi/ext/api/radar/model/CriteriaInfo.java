/**
 * 
 */
package com.asi.ext.api.radar.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Rahul K
 * 
 */
public class CriteriaInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("Code")
    private String            code;
    @JsonProperty("Description")
    private String            description;
    @JsonProperty("DisplayName")
    private String            displayName;
    @JsonProperty("CriteriaTypeCode")
    private String            criteriaTypeCode;
    @JsonProperty("CriteriaTypeDescription")
    private String            criteriaTypeDescription;
    @JsonProperty("CriteriaTypeDisplayName")
    private String            criteriaTypeDisplayName;
    @JsonProperty("DisplaySequence")
    private Integer           displaySequence;
    @JsonProperty("IsAllowBasePrice")
    private Boolean           isAllowBasePrice;
    @JsonProperty("IsAllowUpcharge")
    private Boolean           isAllowUpcharge;
    @JsonProperty("DefaultPriceGridSubTypeCode")
    private String            defaultPriceGridSubTypeCode;
    @JsonProperty("IsFlag")
    private Boolean           isFlag;
    @JsonProperty("IsProductNumberAssignmentAllowed")
    private Boolean           isProductNumberAssignmentAllowed;
    @JsonProperty("IsMediaAssignmentAllowed")
    private Boolean           isMediaAssignmentAllowed;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the criteriaTypeCode
     */
    public String getCriteriaTypeCode() {
        return criteriaTypeCode;
    }

    /**
     * @param criteriaTypeCode
     *            the criteriaTypeCode to set
     */
    public void setCriteriaTypeCode(String criteriaTypeCode) {
        this.criteriaTypeCode = criteriaTypeCode;
    }

    /**
     * @return the criteriaTypeDescription
     */
    public String getCriteriaTypeDescription() {
        return criteriaTypeDescription;
    }

    /**
     * @param criteriaTypeDescription
     *            the criteriaTypeDescription to set
     */
    public void setCriteriaTypeDescription(String criteriaTypeDescription) {
        this.criteriaTypeDescription = criteriaTypeDescription;
    }

    /**
     * @return the criteriaTypeDisplayName
     */
    public String getCriteriaTypeDisplayName() {
        return criteriaTypeDisplayName;
    }

    /**
     * @param criteriaTypeDisplayName
     *            the criteriaTypeDisplayName to set
     */
    public void setCriteriaTypeDisplayName(String criteriaTypeDisplayName) {
        this.criteriaTypeDisplayName = criteriaTypeDisplayName;
    }

    /**
     * @return the displaySequence
     */
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    /**
     * @param displaySequence
     *            the displaySequence to set
     */
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    /**
     * @return the isAllowBasePrice
     */
    public Boolean getIsAllowBasePrice() {
        return isAllowBasePrice;
    }

    /**
     * @param isAllowBasePrice
     *            the isAllowBasePrice to set
     */
    public void setIsAllowBasePrice(Boolean isAllowBasePrice) {
        this.isAllowBasePrice = isAllowBasePrice;
    }

    /**
     * @return the isAllowUpcharge
     */
    public Boolean getIsAllowUpcharge() {
        return isAllowUpcharge;
    }

    /**
     * @param isAllowUpcharge
     *            the isAllowUpcharge to set
     */
    public void setIsAllowUpcharge(Boolean isAllowUpcharge) {
        this.isAllowUpcharge = isAllowUpcharge;
    }

    /**
     * @return the defaultPriceGridSubTypeCode
     */
    public String getDefaultPriceGridSubTypeCode() {
        return defaultPriceGridSubTypeCode;
    }

    /**
     * @param defaultPriceGridSubTypeCode
     *            the defaultPriceGridSubTypeCode to set
     */
    public void setDefaultPriceGridSubTypeCode(String defaultPriceGridSubTypeCode) {
        this.defaultPriceGridSubTypeCode = defaultPriceGridSubTypeCode;
    }

    /**
     * @return the isFlag
     */
    public Boolean getIsFlag() {
        return isFlag;
    }

    /**
     * @param isFlag
     *            the isFlag to set
     */
    public void setIsFlag(Boolean isFlag) {
        this.isFlag = isFlag;
    }

    /**
     * @return the isProductNumberAssignmentAllowed
     */
    public Boolean getIsProductNumberAssignmentAllowed() {
        return isProductNumberAssignmentAllowed;
    }

    /**
     * @param isProductNumberAssignmentAllowed
     *            the isProductNumberAssignmentAllowed to set
     */
    public void setIsProductNumberAssignmentAllowed(Boolean isProductNumberAssignmentAllowed) {
        this.isProductNumberAssignmentAllowed = isProductNumberAssignmentAllowed;
    }

    /**
     * @return the isMediaAssignmentAllowed
     */
    public Boolean getIsMediaAssignmentAllowed() {
        return isMediaAssignmentAllowed;
    }

    /**
     * @param isMediaAssignmentAllowed
     *            the isMediaAssignmentAllowed to set
     */
    public void setIsMediaAssignmentAllowed(Boolean isMediaAssignmentAllowed) {
        this.isMediaAssignmentAllowed = isMediaAssignmentAllowed;
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
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((criteriaTypeCode == null) ? 0 : criteriaTypeCode.hashCode());
        result = prime * result + ((criteriaTypeDescription == null) ? 0 : criteriaTypeDescription.hashCode());
        result = prime * result + ((criteriaTypeDisplayName == null) ? 0 : criteriaTypeDisplayName.hashCode());
        result = prime * result + ((defaultPriceGridSubTypeCode == null) ? 0 : defaultPriceGridSubTypeCode.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((displaySequence == null) ? 0 : displaySequence.hashCode());
        result = prime * result + ((isAllowBasePrice == null) ? 0 : isAllowBasePrice.hashCode());
        result = prime * result + ((isAllowUpcharge == null) ? 0 : isAllowUpcharge.hashCode());
        result = prime * result + ((isFlag == null) ? 0 : isFlag.hashCode());
        result = prime * result + ((isMediaAssignmentAllowed == null) ? 0 : isMediaAssignmentAllowed.hashCode());
        result = prime * result + ((isProductNumberAssignmentAllowed == null) ? 0 : isProductNumberAssignmentAllowed.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CriteriaInfo other = (CriteriaInfo) obj;
        if (code == null) {
            if (other.code != null) return false;
        } else if (!code.equals(other.code)) return false;
        if (criteriaTypeCode == null) {
            if (other.criteriaTypeCode != null) return false;
        } else if (!criteriaTypeCode.equals(other.criteriaTypeCode)) return false;
        if (criteriaTypeDescription == null) {
            if (other.criteriaTypeDescription != null) return false;
        } else if (!criteriaTypeDescription.equals(other.criteriaTypeDescription)) return false;
        if (criteriaTypeDisplayName == null) {
            if (other.criteriaTypeDisplayName != null) return false;
        } else if (!criteriaTypeDisplayName.equals(other.criteriaTypeDisplayName)) return false;
        if (defaultPriceGridSubTypeCode == null) {
            if (other.defaultPriceGridSubTypeCode != null) return false;
        } else if (!defaultPriceGridSubTypeCode.equals(other.defaultPriceGridSubTypeCode)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (displayName == null) {
            if (other.displayName != null) return false;
        } else if (!displayName.equals(other.displayName)) return false;
        if (displaySequence == null) {
            if (other.displaySequence != null) return false;
        } else if (!displaySequence.equals(other.displaySequence)) return false;
        if (isAllowBasePrice == null) {
            if (other.isAllowBasePrice != null) return false;
        } else if (!isAllowBasePrice.equals(other.isAllowBasePrice)) return false;
        if (isAllowUpcharge == null) {
            if (other.isAllowUpcharge != null) return false;
        } else if (!isAllowUpcharge.equals(other.isAllowUpcharge)) return false;
        if (isFlag == null) {
            if (other.isFlag != null) return false;
        } else if (!isFlag.equals(other.isFlag)) return false;
        if (isMediaAssignmentAllowed == null) {
            if (other.isMediaAssignmentAllowed != null) return false;
        } else if (!isMediaAssignmentAllowed.equals(other.isMediaAssignmentAllowed)) return false;
        if (isProductNumberAssignmentAllowed == null) {
            if (other.isProductNumberAssignmentAllowed != null) return false;
        } else if (!isProductNumberAssignmentAllowed.equals(other.isProductNumberAssignmentAllowed)) return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CriteriaInfo [code=" + code + ", description=" + description + ", displayName=" + displayName
                + ", criteriaTypeCode=" + criteriaTypeCode + ", criteriaTypeDescription=" + criteriaTypeDescription
                + ", criteriaTypeDisplayName=" + criteriaTypeDisplayName + ", displaySequence=" + displaySequence
                + ", isAllowBasePrice=" + isAllowBasePrice + ", isAllowUpcharge=" + isAllowUpcharge
                + ", defaultPriceGridSubTypeCode=" + defaultPriceGridSubTypeCode + ", isFlag=" + isFlag
                + ", isProductNumberAssignmentAllowed=" + isProductNumberAssignmentAllowed + ", isMediaAssignmentAllowed="
                + isMediaAssignmentAllowed + "]";
    }

}
