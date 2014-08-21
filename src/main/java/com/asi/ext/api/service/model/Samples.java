package com.asi.ext.api.service.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Samples {

    @JsonProperty("SpecSampleAvailable")
    private Boolean specSampleAvailable;
    @JsonProperty("SpecDetails")
    private String  specDetails;
    @JsonProperty("ProductSampleAvailable")
    private Boolean productSampleAvailable;
    @JsonProperty("ProductSampleDetails")
    private String  productSampleDetails;

    /**
     * @return the specSampleAvailable
     */
    public Boolean getSpecSampleAvailable() {
        return specSampleAvailable;
    }

    /**
     * @param specSampleAvailable
     *            the specSampleAvailable to set
     */
    public void setSpecSampleAvailable(Boolean specSampleAvailable) {
        this.specSampleAvailable = specSampleAvailable;
    }

    /**
     * @return the specDetails
     */
    public String getSpecDetails() {
        return specDetails;
    }

    /**
     * @param specDetails
     *            the specDetails to set
     */
    public void setSpecDetails(String specDetails) {
        this.specDetails = specDetails;
    }

    /**
     * @return the productSampleAvailable
     */
    public Boolean getProductSampleAvailable() {
        return productSampleAvailable;
    }

    /**
     * @param productSampleAvailable
     *            the productSampleAvailable to set
     */
    public void setProductSampleAvailable(Boolean productSampleAvailable) {
        this.productSampleAvailable = productSampleAvailable;
    }

    /**
     * @return the productSampleDetails
     */
    public String getProductSampleDetails() {
        return productSampleDetails;
    }

    /**
     * @param productSampleDetails
     *            the productSampleDetails to set
     */
    public void setProductSampleDetails(String productSampleDetails) {
        this.productSampleDetails = productSampleDetails;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    @JsonIgnore
    public boolean isNull() {
        if (this == null) {
            return true;
        }
        if (this.equals(new Samples())) {
            return true;
        }
        if (this.specSampleAvailable == null && this.productSampleAvailable == null) {
            return true;
        }
        if (this.specSampleAvailable == null && this.productSampleAvailable != null) {
            this.specSampleAvailable = false;
        }
        
        if (this.specSampleAvailable != null && this.productSampleAvailable == null) {
            this.productSampleAvailable = false;
        }
        return false;
    }

}
