package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CriteriaSetCodeValues {
	@JsonProperty("CriteriaSetValueId")
	private String criteriaSetValueId="";
	@JsonProperty("SetCodeValueId")
	private String setCodeValueId="";
	@JsonProperty("CodeValue")
	private String codeValue="";
	@JsonProperty("ID")
	private String id="0";
	@JsonProperty("CodeValueDetail")
	private String codeValueDetail="";
	public CriteriaSetCodeValues()
	{
		//super();
	}
	public CriteriaSetCodeValues(CriteriaSetCodeValues crntObj)
	{
		this.childCriteriaSetCodeValues=crntObj.childCriteriaSetCodeValues;
		this.codeValue=crntObj.codeValue;
		this.codeValueDetail=crntObj.codeValueDetail;
		this.criteriaSetValueId=crntObj.criteriaSetValueId;
		this.id=crntObj.id;
		this.setCodeValueId=crntObj.setCodeValueId;		
	}
	public String getCodeValueDetail() {
		return codeValueDetail;
	}
	public void setCodeValueDetail(String codeValueDetail) {
		this.codeValueDetail = codeValueDetail;
	}
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
	
	public ChildCriteriaSetCodeValues[] getChildCriteriaSetCodeValues() {
		return childCriteriaSetCodeValues;
	}
	public void setChildCriteriaSetCodeValues(
			ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues) {
		this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
	}
	@JsonProperty("ChildCriteriaSetCodeValues")
	private ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues={};

}
