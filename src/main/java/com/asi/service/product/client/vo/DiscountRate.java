package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountRate {
	@JsonProperty("Code")
	private String code="PPPP";
	@JsonProperty("DiscountPercent")
	private String discountPercent="0.5";
	@JsonProperty("IndustryDiscountCode")
	private String industryDiscountCode="";
	@JsonProperty("DiscountValue")
	private String discountValue="";
	@JsonProperty("DisplayValue")
	private String displayValue="";
	@JsonProperty("IsNullo")
	private String isNullo="false";
	@JsonProperty("Description")
private String description="";
public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getDiscountPercent() {
	return discountPercent;
}

public void setDiscountPercent(String discountPercent) {
	this.discountPercent = discountPercent;
}

public String getIndustryDiscountCode() {
	return industryDiscountCode;
}

public void setIndustryDiscountCode(String industryDiscountCode) {
	this.industryDiscountCode = industryDiscountCode;
}

public String getDiscountValue() {
	return discountValue;
}

public void setDiscountValue(String discountValue) {
	this.discountValue = discountValue;
}

public String getDisplayValue() {
	return displayValue;
}

public void setDisplayValue(String displayValue) {
	this.displayValue = displayValue;
}

public String getIsNullo() {
	return isNullo;
}

public void setIsNullo(String isNullo) {
	this.isNullo = isNullo;
} 
}
