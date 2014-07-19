package com.asi.ext.api.service.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {

    @JsonProperty("Sequence")
    private String    sequence;
    @JsonProperty("Qty")
    private String    qty;
    @JsonProperty("ListPrice")
    private String    listPrice;
    @JsonProperty("DiscountCode")
    private String    discountCode;
    @JsonProperty("PriceUnit")
    private PriceUnit priceUnit;

    @JsonProperty("Sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("Sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("Qty")
    public String getQty() {
        return qty;
    }

    @JsonProperty("Qty")
    public void setQty(String qty) {
        this.qty = qty;
    }

    @JsonProperty("ListPrice")
    public String getListPrice() {
        return listPrice;
    }

    @JsonProperty("ListPrice")
    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    @JsonProperty("DiscountCode")
    public String getDiscountCode() {
        return discountCode;
    }

    @JsonProperty("DiscountCode")
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    @JsonProperty("PriceUnit")
    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    @JsonProperty("PriceUnit")
    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
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
