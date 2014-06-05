package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductDataSheet {
@JsonProperty("ID")
private String id;
private String productId="0";
@JsonProperty("companyId")
private String companyId="";
private String url="";

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

public String getCompanyId() {
	return companyId;
}

public void setCompanyId(String companyId) {
	this.companyId = companyId;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}
}
