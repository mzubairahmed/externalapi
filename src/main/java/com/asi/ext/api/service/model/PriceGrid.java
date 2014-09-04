package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PriceGrid {

    @JsonProperty("IsBasePrice")
    private Boolean                  isBasePrice;
    @JsonProperty("IsQUR")
    private Boolean                  isQUR;
    @JsonProperty("Description")
    private String                   description;
    @JsonProperty("PriceIncludes")
    private String                   priceIncludes;
    @JsonProperty("Sequence")
    private Integer                  sequence;
    @JsonProperty("Currency")
    private String                   currency;
    @JsonProperty("ProductNumber")
    private String                   productNumber;
    @JsonProperty("UpchargeType")
    private String                   upchargeType;
    @JsonProperty("UpchargeUsageType")
    private String                   upchargeUsageType;
    @JsonProperty("Prices")
    private List<Price>              prices              = new ArrayList<Price>();
    @JsonProperty("PriceConfigurations")
    private List<PriceConfiguration> priceConfigurations = new ArrayList<PriceConfiguration>();

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

    /**
     * @return the upchargeType
     */
    public String getUpchargeType() {
        return upchargeType;
    }

    /**
     * @param upchargeType
     *            the upchargeType to set
     */
    public void setUpchargeType(String upchargeType) {
        this.upchargeType = upchargeType;
    }

    /**
     * @return the upchargeUsageType
     */
    public String getUpchargeUsageType() {
        return upchargeUsageType;
    }

    /**
     * @param upchargeUsageType
     *            the upchargeUsageType to set
     */
    public void setUpchargeUsageType(String upchargeUsageType) {
        this.upchargeUsageType = upchargeUsageType;
    }

    @JsonProperty("Prices")
    @XmlElementWrapper(name = "Prices")
    @XmlElement(name = "Price")
    public List<Price> getPrices() {
        return prices;
    }

    @JsonProperty("Prices")
    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @JsonProperty("PriceConfigurations")
    @XmlElementWrapper(name = "PriceConfigurations")
    @XmlElement(name = "PriceConfiguration")
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

}
