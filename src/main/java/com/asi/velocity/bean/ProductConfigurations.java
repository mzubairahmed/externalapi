package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductConfigurations {
@JsonProperty("ID")
private String id="0";
private String productId="";
private String isDefault="";
private String configId="";
private String displayProductNumber="";
private String totalVariations="";
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
public ProductCriteriaSets[] getProductCriteriaSets() {
	return productCriteriaSets;
}
public void setProductCriteriaSets(ProductCriteriaSets[] productCriteriaSets) {
	this.productCriteriaSets = productCriteriaSets;
}
private ProductCriteriaSets[] productCriteriaSets={};
}
