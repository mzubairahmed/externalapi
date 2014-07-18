package com.asi.ext.api.service.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Price {

    @JsonProperty("Sequence")
    private String sequence;
    @JsonProperty("Qty")
    private String qty;
    @JsonProperty("ListPrice")
    private String listPrice;
    @JsonProperty("DiscountCode")
    private String discountCode;
    @JsonProperty("PriceUnit")
    private PriceUnit priceUnit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
