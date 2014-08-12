
package com.asi.service.product.client.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "ProductId",
    "IsBasePrice",
    "IsQUR",
    "PriceGridSubTypeCode",
    "UsageLevelCode",
    "Description",
    "IsRange",
    "IsSpecial",
    "PriceIncludes",
    "DisplaySequence",
    "IsCopy",
    "Currency",
    "Prices",
    "PricingItems"
})
public class PriceGrid {

    @JsonProperty("ID")
    private String iD;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("IsBasePrice")
    private Boolean isBasePrice;
    @JsonProperty("IsQUR")
    private Boolean isQUR;
    @JsonProperty("PriceGridSubTypeCode")
    private String priceGridSubTypeCode;
    @JsonProperty("UsageLevelCode")
    private String usageLevelCode;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("IsRange")
    private Boolean isRange;
    @JsonProperty("IsSpecial")
    private Boolean isSpecial;
    @JsonProperty("PriceIncludes")
    private String priceIncludes;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    @JsonProperty("IsCopy")
    private Boolean isCopy;
    @JsonProperty("Currency")
    private Currency currency;
    @JsonProperty("Prices")
    private List<Price> prices = new ArrayList<Price>();
    @JsonProperty("PricingItems")
    private List<PricingItem> pricingItems = new ArrayList<PricingItem>();
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public String getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(String iD) {
        this.iD = iD;
    }

    @JsonProperty("ProductId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    @JsonProperty("PriceGridSubTypeCode")
    public String getPriceGridSubTypeCode() {
        return priceGridSubTypeCode;
    }

    @JsonProperty("PriceGridSubTypeCode")
    public void setPriceGridSubTypeCode(String priceGridSubTypeCode) {
        this.priceGridSubTypeCode = priceGridSubTypeCode;
    }

    @JsonProperty("UsageLevelCode")
    public String getUsageLevelCode() {
        return usageLevelCode;
    }

    @JsonProperty("UsageLevelCode")
    public void setUsageLevelCode(String usageLevelCode) {
        this.usageLevelCode = usageLevelCode;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("IsRange")
    public Boolean getIsRange() {
        return isRange;
    }

    @JsonProperty("IsRange")
    public void setIsRange(Boolean isRange) {
        this.isRange = isRange;
    }

    @JsonProperty("IsSpecial")
    public Boolean getIsSpecial() {
        return isSpecial;
    }

    @JsonProperty("IsSpecial")
    public void setIsSpecial(Boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    @JsonProperty("PriceIncludes")
    public String getPriceIncludes() {
        return priceIncludes;
    }

    @JsonProperty("PriceIncludes")
    public void setPriceIncludes(String priceIncludes) {
        this.priceIncludes = priceIncludes;
    }

    @JsonProperty("DisplaySequence")
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    @JsonProperty("DisplaySequence")
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    @JsonProperty("IsCopy")
    public Boolean getIsCopy() {
        return isCopy;
    }

    @JsonProperty("IsCopy")
    public void setIsCopy(Boolean isCopy) {
        this.isCopy = isCopy;
    }

    @JsonProperty("Currency")
    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonProperty("Prices")
    public List<Price> getPrices() {
        return prices;
    }

    @JsonProperty("Prices")
    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @JsonProperty("PricingItems")
    public List<PricingItem> getPricingItems() {
        return pricingItems;
    }

    @JsonProperty("PricingItems")
    public void setPricingItems(List<PricingItem> pricingItems) {
        this.pricingItems = pricingItems;
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

  /*  @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
