package com.asi.service.product.client.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class ProductConfigurations {
@JsonProperty("ID")
private String id="0";
@JsonProperty("ProductId")
private String productId="";
@JsonProperty("IsDefault")
private String isDefault="";
@JsonProperty("ConfigId")
private String configId="";
@JsonProperty("DisplayProductNumber")
private String displayProductNumber="";
@JsonProperty("TotalVariations")
private String totalVariations="";
@JsonProperty("PricingConfigurations")
private PricingConfigurations[] pricingConfigurations={};
public PricingConfigurations[] getPricingConfigurations() {
	return pricingConfigurations;
}
public void setPricingConfigurations(
		PricingConfigurations[] pricingConfigurations) {
	this.pricingConfigurations = pricingConfigurations;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public String getIsDefault() {
	return isDefault;
}
public void setIsDefault(String isDefault) {
	this.isDefault = isDefault;
}
public String getConfigId() {
	return configId;
}
public void setConfigId(String configId) {
	this.configId = configId;
}
public String getDisplayProductNumber() {
	return displayProductNumber;
}
public void setDisplayProductNumber(String displayProductNumber) {
	this.displayProductNumber = displayProductNumber;
}
public String getTotalVariations() {
	return totalVariations;
}
public void setTotalVariations(String totalVariations) {
	this.totalVariations = totalVariations;
}
public List<ProductCriteriaSets> getProductCriteriaSets() {
	return productCriteriaSets;
}
public void setProductCriteriaSets(List<ProductCriteriaSets> productCriteriaSets) {
	this.productCriteriaSets = productCriteriaSets;
}
@JsonProperty("ProductCriteriaSets")
private List<ProductCriteriaSets> productCriteriaSets=new ArrayList<>();
}
