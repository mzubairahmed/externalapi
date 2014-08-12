package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriteriaSetRelationships {
	
	@JsonProperty("RelationshipId")
	private String relationshipId="0";
	@JsonProperty("CriteriaSetId")
     private String criteriaSetId="0";
	@JsonProperty("ProductId")
     private String productId="0";
	@JsonProperty("IsParent")
     private String isParent="false";
	public String getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	public String getCriteriaSetId() {
		return criteriaSetId;
	}
	public void setCriteriaSetId(String criteriaSetId) {
		this.criteriaSetId = criteriaSetId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
}
