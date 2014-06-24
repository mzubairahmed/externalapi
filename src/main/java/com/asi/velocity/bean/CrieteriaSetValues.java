package com.asi.velocity.bean;

public class CrieteriaSetValues {
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
	private String crieteriaCode;
	private String valueTypeCode;
	private String productNumber;
	private CriteriaSetCodeValues criteriaSetCodeValues;
}
