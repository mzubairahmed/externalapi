package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Relationships {
	@JsonProperty("ID") 
	  private String id;
	  public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getParentCriteriaSetId() {
		return ParentCriteriaSetId;
	}
	public void setParentCriteriaSetId(String parentCriteriaSetId) {
		ParentCriteriaSetId = parentCriteriaSetId;
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
	private String Name;
	@JsonProperty("ProductId")
      private String ProductId;
	@JsonProperty("ParentCriteriaSetId")
      private String ParentCriteriaSetId;
	@JsonProperty("CriteriaSetRelationships")
	private CriteriaSetRelationships[] criteriaSetRelationships;
	@JsonProperty("CriteriaSetValuePaths")
	private CriteriaSetValuePaths[] criteriaSetValuePaths;
}
