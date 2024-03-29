package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountRate {
	@JsonProperty("Code")
	private String code = "PPPP";
	@JsonProperty("Description")
	private String description = "";
	@JsonProperty("DiscountPercent")
	private String discountPercent = "0.5";
	@JsonProperty("IndustryDiscountCode")
	private String industryDiscountCode = "";
	@JsonProperty("DiscountValue")
	private String discountValue = "";
	@JsonProperty("DisplayValue")
	private String displayValue = "";
	@JsonProperty("IsNullo")
	private String isNullo = "false";

	public DiscountRate() {
		// Default constructor
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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

	/**
	 * @return the discountPercent
	 */
	public String getDiscountPercent() {
		return discountPercent;
	}

	/**
	 * @param discountPercent
	 *            the discountPercent to set
	 */
	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	/**
	 * @return the industryDiscountCode
	 */
	public String getIndustryDiscountCode() {
		return industryDiscountCode;
	}

	/**
	 * @param industryDiscountCode
	 *            the industryDiscountCode to set
	 */
	public void setIndustryDiscountCode(String industryDiscountCode) {
		this.industryDiscountCode = industryDiscountCode;
	}

	/**
	 * @return the discountValue
	 */
	public String getDiscountValue() {
		return discountValue;
	}

	/**
	 * @param discountValue
	 *            the discountValue to set
	 */
	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}

	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * @param displayValue
	 *            the displayValue to set
	 */
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return the isNullo
	 */
	public String getIsNullo() {
		return isNullo;
	}

	/**
	 * @param isNullo
	 *            the isNullo to set
	 */
	public void setIsNullo(String isNullo) {
		this.isNullo = isNullo;
	}

}
