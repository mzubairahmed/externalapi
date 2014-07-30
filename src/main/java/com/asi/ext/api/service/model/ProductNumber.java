package com.asi.ext.api.service.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductNumber {

    @JsonProperty("ProductNumber")
    private String productNumber;
    @JsonProperty("Criteria")
    private List<Criteria> criteria;
    

    @JsonProperty("ProductNumber")
    public String getProductNumber() {
        return productNumber;
    }

    @JsonProperty("ProductNumber")
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    @JsonProperty("Criteria")
    public List<Criteria> getCriteria() {
        return criteria;
    }

    @JsonProperty("Criteria")
    public void setCriteria(List<Criteria> criteria) {
        this.criteria = criteria;
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

}
