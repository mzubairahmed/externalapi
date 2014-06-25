package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriteriaSetRelationships {
	 public String getRelationshipId() {
		return RelationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		RelationshipId = relationshipId;
	}
	public String getCriteriaSetId() {
		return CriteriaSetId;
	}
	public void setCriteriaSetId(String criteriaSetId) {
		CriteriaSetId = criteriaSetId;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getIsParent() {
		return IsParent;
	}
	public void setIsParent(String isParent) {
		IsParent = isParent;
	}
	@JsonProperty("RelationshipId")
	private String RelationshipId="0";
	@JsonProperty("CriteriaSetId")
     private String CriteriaSetId="0";
	@JsonProperty("ProductId")
     private String ProductId="0";
	@JsonProperty("IsParent")
     private String IsParent="false";
}
