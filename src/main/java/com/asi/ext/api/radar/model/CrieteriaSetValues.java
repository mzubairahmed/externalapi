package com.asi.ext.api.radar.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class CrieteriaSetValues {
	@JsonProperty("ID")
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCrieteriaCode() {
		return crieteriaCode;
	}
	public void setCrieteriaCode(String crieteriaCode) {
		this.crieteriaCode = crieteriaCode;
	}
	public String getValueTypeCode() {
		return valueTypeCode;
	}
	public void setValueTypeCode(String valueTypeCode) {
		this.valueTypeCode = valueTypeCode;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public CriteriaSetCodeValues getCriteriaSetCodeValues() {
		return criteriaSetCodeValues;
	}
	public void setCriteriaSetCodeValues(CriteriaSetCodeValues criteriaSetCodeValues) {
		this.criteriaSetCodeValues = criteriaSetCodeValues;
	}
	@JsonProperty("CriteriaCode")
	private String crieteriaCode;
	@JsonProperty("ValueTypeCode")
	private String valueTypeCode;
	@JsonProperty("ProductNumber")
	private String productNumber;
	@JsonProperty("CriteriaSetCodeValues")
	private CriteriaSetCodeValues criteriaSetCodeValues;
}
