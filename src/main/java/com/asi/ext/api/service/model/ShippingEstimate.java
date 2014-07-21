package com.asi.ext.api.service.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingEstimate {

    @JsonProperty("NumberOfItems")
    private NumberOfItems numberOfItems;
    @JsonProperty("Dimensions")
    private Dimensions    dimensions;
    @JsonProperty("Weight")
    private Weight        weight;

    @JsonProperty("NumberOfItems")
    public NumberOfItems getNumberOfItems() {
        return numberOfItems;
    }

    @JsonProperty("NumberOfItems")
    public void setNumberOfItems(NumberOfItems numberOfItems) {
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
    public Weight getWeight() {
        return weight;
    }

    @JsonProperty("Weight")
    public void setWeight(Weight weight) {
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
