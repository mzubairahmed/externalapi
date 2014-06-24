package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class Value {
@JsonProperty("CriteriaAttributeId")
private String criteriaAttributeId="";
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
@JsonProperty("UnitValue")
private String unitValue="";
@JsonProperty("UnitOfMeasureCode")
private String unitOfMeasureCode="";

}
