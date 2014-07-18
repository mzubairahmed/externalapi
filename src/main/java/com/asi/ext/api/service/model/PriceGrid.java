package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class PriceGrid {

    @JsonProperty("IsBasePrice")
    private Boolean isBasePrice;
    @JsonProperty("IsQUR")
    private Boolean isQUR;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("PriceIncludes")
    private String priceIncludes;
    @JsonProperty("Sequence")
    private Integer sequence;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("ProductNumber")
    private String productNumber;
    @JsonProperty("Prices")
    private List<Price> prices = new ArrayList<Price>();
    @JsonProperty("PriceConfigurations")
    private List<PriceConfiguration> priceConfigurations = new ArrayList<PriceConfiguration>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("IsBasePrice")
    public Boolean getIsBasePrice() {
        return isBasePrice;
    }

    @JsonProperty("IsBasePrice")
    public void setIsBasePrice(Boolean isBasePrice) {
        this.isBasePrice = isBasePrice;
    }

    @JsonProperty("IsQUR")
    public Boolean getIsQUR() {
        return isQUR;
    }

    @JsonProperty("IsQUR")
    public void setIsQUR(Boolean isQUR) {
        this.isQUR = isQUR;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("PriceIncludes")
    public String getPriceIncludes() {
        return priceIncludes;
    }

    @JsonProperty("PriceIncludes")
    public void setPriceIncludes(String priceIncludes) {
        this.priceIncludes = priceIncludes;
    }

    @JsonProperty("Sequence")
    public Integer getSequence() {
        return sequence;
    }

    @JsonProperty("Sequence")
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("ProductNumber")
    public String getProductNumber() {
        return productNumber;
    }

    @JsonProperty("ProductNumber")
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    @JsonProperty("Prices")
    public List<Price> getPrices() {
        return prices;
    }

    @JsonProperty("Prices")
    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @JsonProperty("PriceConfigurations")
    public List<PriceConfiguration> getPriceConfigurations() {
        return priceConfigurations;
    }

    @JsonProperty("PriceConfigurations")
    public void setPriceConfigurations(List<PriceConfiguration> priceConfigurations) {
        this.priceConfigurations = priceConfigurations;
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
