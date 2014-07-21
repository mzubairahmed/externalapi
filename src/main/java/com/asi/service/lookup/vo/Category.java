package com.asi.service.lookup.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "categoriesList")
@XmlType(propOrder={"code","description","name","displayName","parentCategoryCode","productTypeCode","isProductTypeSpecific","isAllowsAssign","isParent","isPrimary"})
@JsonPropertyOrder({"code","description","name","displayName","parentCategoryCode","productTypeCode","isProductTypeSpecific","isAllowsAssign","isParent","isPrimary"})
public class Category {
	@XmlElement(nillable=true, name="code")
	private String code;
	@XmlElement(nillable=true, name="description")
	private String description;
	@XmlElement(nillable=true, name="name")
	private String name;
	@XmlElement(nillable=true, name="displayName")
	private String displayName;
	@XmlElement(nillable=true, name="parentCategoryCode")
	private String parentCategoryCode;
	@XmlElement(nillable=true, name="productTypeCode")
	private String productTypeCode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getParentCategoryCode() {
		return parentCategoryCode;
	}
	public void setParentCategoryCode(String parentCategoryCode) {
		this.parentCategoryCode = parentCategoryCode;
	}
	public String getProductTypeCode() {
		return productTypeCode;
	}
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}
	public String getIsProductTypeSpecific() {
		return isProductTypeSpecific;
	}
	public void setIsProductTypeSpecific(String isProductTypeSpecific) {
		this.isProductTypeSpecific = isProductTypeSpecific;
	}
	public String getIsAllowsAssign() {
		return isAllowsAssign;
	}
	public void setIsAllowsAssign(String isAllowsAssign) {
		this.isAllowsAssign = isAllowsAssign;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	@XmlElement(nillable=true, name="isProductTypeSpecific")
	private String isProductTypeSpecific;
	@XmlElement(nillable=true, name="isAllowsAssign")
	private String isAllowsAssign;
	@XmlElement(nillable=true, name="isParent")
	private String isParent;
	@XmlElement(nillable=true, name="isPrimary")
	private String isPrimary;
	
}
