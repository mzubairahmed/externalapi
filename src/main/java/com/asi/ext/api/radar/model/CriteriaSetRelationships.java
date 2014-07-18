package com.asi.ext.api.radar.model;

public class CriteriaSetRelationships {

    private String RelationshipId = "0";
    private String CriteriaSetId  = "0";
    private String ProductId      = "0";
    private String IsParent       = "false";

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((CriteriaSetId == null) ? 0 : CriteriaSetId.hashCode());
        result = prime * result + ((IsParent == null) ? 0 : IsParent.hashCode());
        result = prime * result + ((ProductId == null) ? 0 : ProductId.hashCode());
        result = prime * result + ((RelationshipId == null) ? 0 : RelationshipId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CriteriaSetRelationships other = (CriteriaSetRelationships) obj;
        if (CriteriaSetId == null) {
            if (other.CriteriaSetId != null) {
                return false;
            }
        } else if (!CriteriaSetId.equals(other.CriteriaSetId)) {
            return false;
        }
        if (IsParent == null) {
            if (other.IsParent != null) {
                return false;
            }
        } else if (!IsParent.equals(other.IsParent)) {
            return false;
        }
        if (ProductId == null) {
            if (other.ProductId != null) {
                return false;
            }
        } else if (!ProductId.equals(other.ProductId)) {
            return false;
        }
        if (RelationshipId == null) {
            if (other.RelationshipId != null) {
                return false;
            }
        } else if (!RelationshipId.equals(other.RelationshipId)) {
            return false;
        }
        return true;
    }

}
