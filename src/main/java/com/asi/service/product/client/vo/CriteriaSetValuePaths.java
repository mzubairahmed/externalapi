package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CriteriaSetValuePaths {
	@JsonProperty("ID") 
	private String id="0";
     public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("CriteriaSetValueId")
	private String criteriaSetValueId="0";
	@JsonProperty("RelationshipId")
     private String relationshipId="0";
	@JsonProperty("ProductId")
     private String productId="0";
	@JsonProperty("IsParent")
     private String isParent="false";
	public String getCriteriaSetValueId() {
		return criteriaSetValueId;
	}
	public void setCriteriaSetValueId(String criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}
	public String getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
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
     
    /*@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((CriteriaSetValueId == null) ? 0 : CriteriaSetValueId.hashCode());
        result = prime * result + ((IsParent == null) ? 0 : IsParent.hashCode());
        result = prime * result + ((ProductId == null) ? 0 : ProductId.hashCode());
        result = prime * result + ((RelationshipId == null) ? 0 : RelationshipId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CriteriaSetValuePaths other = (CriteriaSetValuePaths) obj;
        if (CriteriaSetValueId == null) {
            if (other.CriteriaSetValueId != null) return false;
        } else if (!CriteriaSetValueId.equals(other.CriteriaSetValueId)) return false;
        if (IsParent == null) {
            if (other.IsParent != null) return false;
        } else if (!IsParent.equals(other.IsParent)) return false;
        if (ProductId == null) {
            if (other.ProductId != null) return false;
        } else if (!ProductId.equals(other.ProductId)) return false;
        if (RelationshipId == null) {
            if (other.RelationshipId != null) return false;
        } else if (!RelationshipId.equals(other.RelationshipId)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }*/ 
     
}