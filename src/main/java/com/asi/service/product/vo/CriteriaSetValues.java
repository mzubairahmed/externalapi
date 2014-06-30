package com.asi.service.product.vo;


import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.SubSets;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CriteriaSetValues {
	@JsonProperty("ID")
	private String id="0";
	@JsonProperty("CriteriaCode")
	private String criteriaCode="SHAP";
	@JsonProperty("ValueTypeCode")
	private String valueTypeCode="";
	@JsonProperty("CriteriaValueDetail")
	private String criteriaValueDetail="";
	@JsonProperty("IsSubset")
	private String isSubset="";
	@JsonProperty("IsSetValueMeasurement")
	private String isSetValueMeasurement="";
	@JsonProperty("CriteriaSetId")
	private String criteriaSetId="";
	@JsonProperty("BaseLookupValue")
	private String baseLookupValue="";
	@JsonProperty("ProductNumber")
	private String productNumber="";
	@JsonProperty("Value")
	private Object value=null;
	@JsonProperty("FormatValue")
	private String formatValue="";
	public String getFormatValue() {
		return formatValue;
	}
	public void setFormatValue(String formatValue) {
		this.formatValue = formatValue;
	}
	@JsonProperty("SubSets")
	private SubSets[] subSets={};
	public SubSets[] getSubSets() {
		return subSets;
	}
	public void setSubSets(SubSets[] subSets) {
		this.subSets = subSets;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCriteriaCode() {
		return criteriaCode;
	}
	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}
	public String getValueTypeCode() {
		return valueTypeCode;
	}
	public void setValueTypeCode(String valueTypeCode) {
		this.valueTypeCode = valueTypeCode;
	}
	public String getCriteriaValueDetail() {
		return criteriaValueDetail;
	}
	public void setCriteriaValueDetail(String criteriaValueDetail) {
		this.criteriaValueDetail = criteriaValueDetail;
	}
	public String getIsSubset() {
		return isSubset;
	}
	public void setIsSubset(String isSubset) {
		this.isSubset = isSubset;
	}
	public String getIsSetValueMeasurement() {
		return isSetValueMeasurement;
	}
	public void setIsSetValueMeasurement(String isSetValueMeasurement) {
		this.isSetValueMeasurement = isSetValueMeasurement;
	}
	public String getCriteriaSetId() {
		return criteriaSetId;
	}
	public void setCriteriaSetId(String criteriaSetId) {
		this.criteriaSetId = criteriaSetId;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getBaseLookupValue() {
		return baseLookupValue;
	}
	public void setBaseLookupValue(String baseLookupValue) {
		this.baseLookupValue = baseLookupValue;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public CriteriaSetCodeValues[] getCriteriaSetCodeValues() {
		return criteriaSetCodeValues;
	}
	public void setCriteriaSetCodeValues(
			CriteriaSetCodeValues[] criteriaSetCodeValues) {
		this.criteriaSetCodeValues = criteriaSetCodeValues;
	}
	@JsonProperty("CriteriaSetCodeValues")
	private CriteriaSetCodeValues[] criteriaSetCodeValues={};
	@JsonProperty("DisplaySequence")
    private String displaySequence="";
    
    public String getDisplaySequence() {
                    return displaySequence;
    }
    public void setDisplaySequence(String displaySequence) {
                    this.displaySequence = displaySequence;
    }

}

