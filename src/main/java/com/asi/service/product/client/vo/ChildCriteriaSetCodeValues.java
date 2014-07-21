package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChildCriteriaSetCodeValues {
	@JsonProperty("CriteriaSetValueId")
private String criteriaSetValueId="";
	@JsonProperty("SetCodeValueId")
private String setCodeValueId="";
	@JsonProperty("CodeValue")
private String codeValue="";
	@JsonProperty("ID")
private String id="";

public String getCriteriaSetValueId() {
	return criteriaSetValueId;
}

public void setCriteriaSetValueId(String criteriaSetValueId) {
	this.criteriaSetValueId = criteriaSetValueId;
}

public String getSetCodeValueId() {
	return setCodeValueId;
}

public void setSetCodeValueId(String setCodeValueId) {
	this.setCodeValueId = setCodeValueId;
}

public String getCodeValue() {
	return codeValue;
}

public void setCodeValue(String codeValue) {
	this.codeValue = codeValue;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}
}
