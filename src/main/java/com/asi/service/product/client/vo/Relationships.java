package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Relationships {
	@JsonProperty("ID") 
	  private String id;
	  public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getParentCriteriaSetId() {
		return parentCriteriaSetId;
	}
	public void setParentCriteriaSetId(String parentCriteriaSetId) {
		this.parentCriteriaSetId = parentCriteriaSetId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CriteriaSetRelationships[] getCriteriaSetRelationships() {
		return criteriaSetRelationships;
	}
	public void setCriteriaSetRelationships(
			CriteriaSetRelationships[] criteriaSetRelationships) {
		this.criteriaSetRelationships = criteriaSetRelationships;
	}
	public CriteriaSetValuePaths[] getCriteriaSetValuePaths() {
		return criteriaSetValuePaths;
	}
	public void setCriteriaSetValuePaths(
			CriteriaSetValuePaths[] criteriaSetValuePaths) {
		this.criteriaSetValuePaths = criteriaSetValuePaths;
	}
	@JsonProperty("Name")
	private String name;
	@JsonProperty("ProductId")
      private String productId;
	@JsonProperty("ParentCriteriaSetId")
      private String parentCriteriaSetId;
	@JsonProperty("CriteriaSetRelationships")
	private CriteriaSetRelationships[] criteriaSetRelationships;
	@JsonProperty("CriteriaSetValuePaths")
	private CriteriaSetValuePaths[] criteriaSetValuePaths;
}
