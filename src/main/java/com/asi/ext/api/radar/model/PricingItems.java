package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PricingItems {

	@JsonProperty("ID")
	private String id;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("CriteriaSetValueId")
	private String criteriaSetValueId;
	@JsonProperty("MarketSegmentCode")
	private String marketSegmentCode;
	@JsonProperty("PriceGridId")
	private String priceGridId;
	@JsonProperty("PriceGridTypeCode")
	private String priceGridTypeCode;
	@JsonProperty("IsPrimary")
	private String isPrimary;
	@JsonProperty("IsDateBased")
	private String isDateBased;
	@JsonProperty("Description")
	private String description;

	public PricingItems() {
		// Default Constructor
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the criteriaSetValueId
	 */
	public String getCriteriaSetValueId() {
		return criteriaSetValueId;
	}

	/**
	 * @param criteriaSetValueId
	 *            the criteriaSetValueId to set
	 */
	public void setCriteriaSetValueId(String criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}

	/**
	 * @return the marketSegmentCode
	 */
	public String getMarketSegmentCode() {
		return marketSegmentCode;
	}

	/**
	 * @param marketSegmentCode
	 *            the marketSegmentCode to set
	 */
	public void setMarketSegmentCode(String marketSegmentCode) {
		this.marketSegmentCode = marketSegmentCode;
	}

	/**
	 * @return the priceGridId
	 */
	public String getPriceGridId() {
		return priceGridId;
	}

	/**
	 * @param priceGridId
	 *            the priceGridId to set
	 */
	public void setPriceGridId(String priceGridId) {
		this.priceGridId = priceGridId;
	}

	/**
	 * @return the priceGridTypeCode
	 */
	public String getPriceGridTypeCode() {
		return priceGridTypeCode;
	}

	/**
	 * @param priceGridTypeCode
	 *            the priceGridTypeCode to set
	 */
	public void setPriceGridTypeCode(String priceGridTypeCode) {
		this.priceGridTypeCode = priceGridTypeCode;
	}

	/**
	 * @return the isPrimary
	 */
	public String getIsPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary
	 *            the isPrimary to set
	 */
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the isDateBased
	 */
	public String getIsDateBased() {
		return isDateBased;
	}

	/**
	 * @param isDateBased
	 *            the isDateBased to set
	 */
	public void setIsDateBased(String isDateBased) {
		this.isDateBased = isDateBased;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
