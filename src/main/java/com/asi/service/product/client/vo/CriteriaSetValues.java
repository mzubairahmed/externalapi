package com.asi.service.product.client.vo;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CriteriaSetValues {
	@JsonProperty("ID")
	private String id="0";
	@JsonProperty("CriteriaCode")
	private String criteriaCode="";
	@JsonProperty("ValueTypeCode")
	private String valueTypeCode="";
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((baseLookupValue == null) ? 0 : baseLookupValue.hashCode());
		result = prime * result
				+ ((criteriaCode == null) ? 0 : criteriaCode.hashCode());
		result = prime * result + Arrays.hashCode(criteriaSetCodeValues);
		result = prime * result
				+ ((criteriaSetId == null) ? 0 : criteriaSetId.hashCode());
		result = prime
				* result
				+ ((criteriaValueDetail == null) ? 0 : criteriaValueDetail
						.hashCode());
		result = prime * result
				+ ((displaySequence == null) ? 0 : displaySequence.hashCode());
		result = prime * result
				+ ((formatValue == null) ? 0 : formatValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((isSetValueMeasurement == null) ? 0 : isSetValueMeasurement
						.hashCode());
		result = prime * result
				+ ((isSubset == null) ? 0 : isSubset.hashCode());
		result = prime * result
				+ ((productNumber == null) ? 0 : productNumber.hashCode());
		result = prime * result + Arrays.hashCode(subSets);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result
				+ ((valueTypeCode == null) ? 0 : valueTypeCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CriteriaSetValues other = (CriteriaSetValues) obj;
		if (baseLookupValue == null) {
			if (other.baseLookupValue != null)
				return false;
		} else if (!baseLookupValue.equals(other.baseLookupValue))
			return false;
		if (criteriaCode == null) {
			if (other.criteriaCode != null)
				return false;
		} else if (!criteriaCode.equals(other.criteriaCode))
			return false;
		if (!Arrays.equals(criteriaSetCodeValues, other.criteriaSetCodeValues))
			return false;
		if (criteriaSetId == null) {
			if (other.criteriaSetId != null)
				return false;
		} else if (!criteriaSetId.equals(other.criteriaSetId))
			return false;
		if (criteriaValueDetail == null) {
			if (other.criteriaValueDetail != null)
				return false;
		} else if (!criteriaValueDetail.equals(other.criteriaValueDetail))
			return false;
		if (displaySequence == null) {
			if (other.displaySequence != null)
				return false;
		} else if (!displaySequence.equals(other.displaySequence))
			return false;
		if (formatValue == null) {
			if (other.formatValue != null)
				return false;
		} else if (!formatValue.equals(other.formatValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isSetValueMeasurement == null) {
			if (other.isSetValueMeasurement != null)
				return false;
		} else if (!isSetValueMeasurement.equals(other.isSetValueMeasurement))
			return false;
		if (isSubset == null) {
			if (other.isSubset != null)
				return false;
		} else if (!isSubset.equals(other.isSubset))
			return false;
		if (productNumber == null) {
			if (other.productNumber != null)
				return false;
		} else if (!productNumber.equals(other.productNumber))
			return false;
		if (!Arrays.equals(subSets, other.subSets))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (valueTypeCode == null) {
			if (other.valueTypeCode != null)
				return false;
		} else if (!valueTypeCode.equals(other.valueTypeCode))
			return false;
		return true;
	}
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
