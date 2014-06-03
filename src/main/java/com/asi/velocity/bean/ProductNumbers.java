package com.asi.velocity.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductNumbers {
@JsonProperty("ID")
private String id;
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
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}

public List<ProductNumberConfigurations> getProductNumberConfigurations() {
	return productNumberConfigurations;
}
public void setProductNumberConfigurations(
		List<ProductNumberConfigurations> productNumberConfigurations) {
	this.productNumberConfigurations = productNumberConfigurations;
}

private String productId="";
private String value="";
private List<ProductNumberConfigurations> productNumberConfigurations=new ArrayList<>();


}
