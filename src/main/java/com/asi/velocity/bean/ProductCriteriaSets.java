package com.asi.velocity.bean;

import java.io.Serializable;

public class ProductCriteriaSets implements Serializable {
	
	public ProductCriteriaSets()
	{
		
	}
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public String getCriteriaSetId() {
		return criteriaSetId;
	}
	public void setCriteriaSetId(String criteriaSetId) {
		this.criteriaSetId = criteriaSetId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getParentCriteriaSetId() {
		return parentCriteriaSetId;
	}
	public void setParentCriteriaSetId(String parentCriteriaSetId) {
		this.parentCriteriaSetId = parentCriteriaSetId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCriteriaCode() {
		return criteriaCode;
	}
	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
	public String getIsRequiredForOrder() {
		return isRequiredForOrder;
	}
	public void setIsRequiredForOrder(String isRequiredForOrder) {
		this.isRequiredForOrder = isRequiredForOrder;
	}
	public String getIsMultipleChoiceAllowed() {
		return isMultipleChoiceAllowed;
	}
	public void setIsMultipleChoiceAllowed(String isMultipleChoiceAllowed) {
		this.isMultipleChoiceAllowed = isMultipleChoiceAllowed;
	}
	public String getIsTemplate() {
		return isTemplate;
	}
	public void setIsTemplate(String isTemplate) {
		this.isTemplate = isTemplate;
	}
	public String getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}
	public String getCriteriaDetail() {
		return criteriaDetail;
	}
	public void setCriteriaDetail(String criteriaDetail) {
		this.criteriaDetail = criteriaDetail;
	}
	public String getIsDefaultConfiguration() {
		return isDefaultConfiguration;
	}
	public void setIsDefaultConfiguration(String isDefaultConfiguration) {
		this.isDefaultConfiguration = isDefaultConfiguration;
	}
	public String getDisplayProductNumber() {
		return displayProductNumber;
	}
	public void setDisplayProductNumber(String displayProductNumber) {
		this.displayProductNumber = displayProductNumber;
	}
	public String getDisplayOptionName() {
		return displayOptionName;
	}
	public void setDisplayOptionName(String displayOptionName) {
		this.displayOptionName = displayOptionName;
	}
	public String getIsBrokenOutOn() {
		return isBrokenOutOn;
	}
	public void setIsBrokenOutOn(String isBrokenOutOn) {
		this.isBrokenOutOn = isBrokenOutOn;
	}
	public CriteriaSetValues[] getCriteriaSetValues() {
		return criteriaSetValues;
	}
	public void setCriteriaSetValues(CriteriaSetValues[] criteriaSetValues) {
		this.criteriaSetValues = criteriaSetValues;
	}
	
	public ProductCriteriaSets(String criteriaSetId,String productId,String companyId,String configId,String parentCriteriaSetId,String description,String criteriaCode,String isBase,String isRequiredForOrder,String isMultipleChoiceAllowed,String isTemplate,String orderDetail,String criteriaDetail,String isDefaultConfiguration,String displayProductNumber,String displayOptionName,String isBrokenOutOn,CriteriaSetValues[] criteriaSetValues)
	{
		this.criteriaSetId=criteriaSetId;
		this.criteriaSetId=criteriaSetId;
		this.productId=productId;
		this.companyId=companyId;
		this.configId=configId;
		this.parentCriteriaSetId=parentCriteriaSetId;
		this.description=description;
		this.criteriaCode=criteriaCode;
		this.isBase=isBase;
		this.isRequiredForOrder=isRequiredForOrder;
		this.isMultipleChoiceAllowed=isMultipleChoiceAllowed;
		this.isTemplate=isTemplate;
		this.orderDetail=orderDetail;
		this.criteriaDetail=criteriaDetail;
		this.isDefaultConfiguration=isDefaultConfiguration;
		this.displayProductNumber=displayProductNumber;
		this.displayOptionName=displayOptionName;
		this.isBrokenOutOn=isBrokenOutOn;
		this.criteriaSetValues=criteriaSetValues;
	}
	public ProductCriteriaSets copyAllProperties()
	{
		ProductCriteriaSets finalObj=new ProductCriteriaSets();
		finalObj.criteriaSetId=this.criteriaSetId;
		finalObj.productId=this.productId;
		finalObj.companyId=this.companyId;
		finalObj.configId=this.configId;
		finalObj.parentCriteriaSetId=this.parentCriteriaSetId;
		finalObj.description=this.description;
		finalObj.criteriaCode=this.criteriaCode;
		finalObj.isBase=this.isBase;
		finalObj.isRequiredForOrder=this.isRequiredForOrder;
		finalObj.isMultipleChoiceAllowed=this.isMultipleChoiceAllowed;
		finalObj.isTemplate=this.isTemplate;
		finalObj.orderDetail=this.orderDetail;
		finalObj.criteriaDetail=this.criteriaDetail;
		finalObj.isDefaultConfiguration=this.isDefaultConfiguration;
		finalObj.displayProductNumber=this.displayProductNumber;
		finalObj.displayOptionName=this.displayOptionName;
		finalObj.isBrokenOutOn=this.isBrokenOutOn;
		finalObj.criteriaSetValues=this.criteriaSetValues;
		return finalObj;
	}
private String criteriaSetId="",productId="",companyId="",configId="",parentCriteriaSetId="0",description="",criteriaCode="SHAP",isBase="false",isRequiredForOrder="false";
private String isMultipleChoiceAllowed="false",isTemplate="false",orderDetail="",criteriaDetail="",isDefaultConfiguration="true";
private String displayProductNumber="",displayOptionName="",isBrokenOutOn="false";
private CriteriaSetValues[] criteriaSetValues={};
}
