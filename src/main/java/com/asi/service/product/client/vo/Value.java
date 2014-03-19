package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "CriteriaAttributeId",
    "UnitValue",
    "UnitOfMeasureCode"
})
public class Value {
	public String getCriteriaAttributeId() {
		return criteriaAttributeId;
	}
	public void setCriteriaAttributeId(String criteriaAttributeId) {
		this.criteriaAttributeId = criteriaAttributeId;
	}
	public String getUnitValue() {
		return unitValue;
	}
	public void setUnitValue(String unitValue) {
		this.unitValue = unitValue;
	}
	public String getUnitOfMeasureCode() {
		return unitOfMeasureCode;
	}
	public void setUnitOfMeasureCode(String unitOfMeasureCode) {
		this.unitOfMeasureCode = unitOfMeasureCode;
	}
	@JsonProperty("CriteriaAttributeId")
	private String criteriaAttributeId="";
	@JsonProperty("UnitValue")
	private String unitValue="";
	@JsonProperty("UnitOfMeasureCode")
	private String unitOfMeasureCode="";
}
