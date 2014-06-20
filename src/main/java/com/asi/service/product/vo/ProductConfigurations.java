package com.asi.service.product.vo;


import org.codehaus.jackson.annotate.JsonProperty;

public class ProductConfigurations {
@JsonProperty("ID")
private String id="0";
private String productId="";
private String isDefault="";
private ProductCriteriaSets productCriteriaSets;

public ProductCriteriaSets getProductCriteriaSets() {
	return productCriteriaSets;
}
public void setProductCriteriaSets(ProductCriteriaSets productCriteriaSets) {
	this.productCriteriaSets = productCriteriaSets;
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

}

