package com.asi.ext.api.radar.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductNumberConfigurations {
 public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductNumberId() {
		return productNumberId;
	}
	public void setProductNumberId(String productNumberId) {
		this.productNumberId = productNumberId;
	}
	public String getCriteriaSetValueId() {
		return criteriaSetValueId;
	}
	public void setCriteriaSetValueId(String criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}
	@JsonProperty("ID") 
	private String id="";
 private String productNumberId="";
 private String criteriaSetValueId="";
 
}
