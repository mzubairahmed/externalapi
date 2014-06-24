package com.asi.service.product.client.vo;

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
	private String RelationshipId="0";
     private String CriteriaSetId="0";
     private String ProductId="0";
     private String IsParent="false";
}
