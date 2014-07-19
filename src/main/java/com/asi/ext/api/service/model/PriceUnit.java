package com.asi.ext.api.service.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceUnit {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("ItemsPerUnit")
    private String itemsPerUnit;
    @JsonProperty("PriceUnitName")
    private String priceUnitName;

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ItemsPerUnit")
    public String getItemsPerUnit() {
        return itemsPerUnit;
    }

    @JsonProperty("ItemsPerUnit")
    public void setItemsPerUnit(String itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    @JsonProperty("PriceUnitName")
    public String getPriceUnitName() {
        return priceUnitName;
    }

    @JsonProperty("PriceUnitName")
    public void setPriceUnitName(String priceUnitName) {
        this.priceUnitName = priceUnitName;
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
