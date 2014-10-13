package com.asi.ext.api.service.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class ShippingEstimate {

    @JsonProperty("NumberOfItems")
    private List<Object> numberOfItems;
    @JsonProperty("Dimensions")
    private Dimensions    dimensions;
    @JsonProperty("Weight")
    private List<Object>       weight;

    @JsonProperty("NumberOfItems")
    public List<Object> getNumberOfItems() {
        return numberOfItems;
    }

    @JsonProperty("NumberOfItems")
    public void setNumberOfItems(List<Object> numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    @JsonProperty("Dimensions")
    public Dimensions getDimensions() {
        return dimensions;
    }

    @JsonProperty("Dimensions")
    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @JsonProperty("Weight")
    public List<Object> getWeight() {
        return weight;
    }

    @JsonProperty("Weight")
    public void setWeight(List<Object> weight) {
        this.weight = weight;
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
