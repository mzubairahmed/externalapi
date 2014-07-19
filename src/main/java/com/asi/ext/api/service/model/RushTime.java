package com.asi.ext.api.service.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RushTime {

    @JsonProperty("BusinessDays")
    private Integer businessDays;
    @JsonProperty("Details")
    private String  details;

    @JsonProperty("BusinessDays")
    public Integer getBusinessDays() {
        return businessDays;
    }

    @JsonProperty("BusinessDays")
    public void setBusinessDays(Integer businessDays) {
        this.businessDays = businessDays;
    }

    @JsonProperty("Details")
    public String getDetails() {
        return details;
    }

    @JsonProperty("Details")
    public void setDetails(String details) {
        this.details = details;
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
