
package com.asi.service.product.client.vo;

import java.util.HashMap;
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
    "PriceGridId",
    "SequenceNumber",
    "Quantity",
    "ListPrice",
    "NetCost",
    "MaxDecimalPlaces",
    "ItemsPerUnit",
    "PriceUnitName",
    "PriceUnit",
    "DiscountRate",
    "LowQuantity",
    "HighQuantity"
})
public class Price {

    @JsonProperty("PriceGridId")
    private String priceGridId;
    @JsonProperty("SequenceNumber")
    private Integer sequenceNumber;
    @JsonProperty("Quantity")
    private Integer quantity;
    @JsonProperty("ListPrice")
    private Double listPrice;
    @JsonProperty("NetCost")
    private Double netCost;
    @JsonProperty("MaxDecimalPlaces")
    private Integer maxDecimalPlaces;
    @JsonProperty("ItemsPerUnit")
    private Integer itemsPerUnit;
    @JsonProperty("PriceUnitName")
    private String priceUnitName;
    @JsonProperty("PriceUnit")
    private PriceUnit priceUnit;
    @JsonProperty("DiscountRate")
    private DiscountRate discountRate;
    @JsonProperty("LowQuantity")
    private Integer lowQuantity;
    @JsonProperty("HighQuantity")
    private Integer highQuantity;    
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
	 * @return the lowQuantity
	 */
    @JsonProperty("LowQuantity")
	public Integer getLowQuantity() {
		return lowQuantity;
	}

	/**
	 * @param lowQuantity the lowQuantity to set
	 */
	 @JsonProperty("LowQuantity")
	public void setLowQuantity(Integer lowQuantity) {
		this.lowQuantity = lowQuantity;
	}

	/**
	 * @return the highQuantity
	 */
	 @JsonProperty("HighQuantity")
	public Integer getHighQuantity() {
		return highQuantity;
	}

	/**
	 * @param highQuantity the highQuantity to set
	 */
	 @JsonProperty("HighQuantity")
	public void setHighQuantity(Integer highQuantity) {
		this.highQuantity = highQuantity;
	}

	@JsonProperty("PriceGridId")
    public String getPriceGridId() {
        return priceGridId;
    }

    @JsonProperty("PriceGridId")
    public void setPriceGridId(String priceGridId) {
        this.priceGridId = priceGridId;
    }

    @JsonProperty("SequenceNumber")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @JsonProperty("SequenceNumber")
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @JsonProperty("Quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("ListPrice")
    public Double getListPrice() {
        return listPrice;
    }

    @JsonProperty("ListPrice")
    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    @JsonProperty("NetCost")
    public Double getNetCost() {
        return netCost;
    }

    @JsonProperty("NetCost")
    public void setNetCost(Double netCost) {
        this.netCost = netCost;
    }

    @JsonProperty("MaxDecimalPlaces")
    public Integer getMaxDecimalPlaces() {
        return maxDecimalPlaces;
    }

    @JsonProperty("MaxDecimalPlaces")
    public void setMaxDecimalPlaces(Integer maxDecimalPlaces) {
        this.maxDecimalPlaces = maxDecimalPlaces;
    }

    @JsonProperty("ItemsPerUnit")
    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    @JsonProperty("ItemsPerUnit")
    public void setItemsPerUnit(Integer itemsPerUnit) {
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

    @JsonProperty("PriceUnit")
    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    @JsonProperty("PriceUnit")
    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    @JsonProperty("DiscountRate")
    public DiscountRate getDiscountRate() {
        return discountRate;
    }

    @JsonProperty("DiscountRate")
    public void setDiscountRate(DiscountRate discountRate) {
        this.discountRate = discountRate;
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
