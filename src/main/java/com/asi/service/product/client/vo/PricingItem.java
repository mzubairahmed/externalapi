package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingItem {
	@JsonProperty("ID")
	private String id;
	private String productId;
	private String criteriaSetValueId;
	private String marketSegmentCode;
	private String priceGridId;
	private String priceGridTypeCode;
	private String isPrimary;
	private String description;
	private String isDateBased;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCriteriaSetValueId() {
		return criteriaSetValueId;
	}

	public void setCriteriaSetValueId(String criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}

	public String getMarketSegmentCode() {
		return marketSegmentCode;
	}

	public void setMarketSegmentCode(String marketSegmentCode) {
		this.marketSegmentCode = marketSegmentCode;
	}

	public String getPriceGridId() {
		return priceGridId;
	}

	public void setPriceGridId(String priceGridId) {
		this.priceGridId = priceGridId;
	}

	public String getPriceGridTypeCode() {
		return priceGridTypeCode;
	}

	public void setPriceGridTypeCode(String priceGridTypeCode) {
		this.priceGridTypeCode = priceGridTypeCode;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDateBased() {
		return isDateBased;
	}

	public void setIsDateBased(String isDateBased) {
		this.isDateBased = isDateBased;
	}

}
