package com.asi.ext.api.radar.model;

import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonProperty;

public class Relationships {
    @JsonProperty("ID")
    private String                     id;
    private String                     Name;
    private String                     ProductId;
    private String                     ParentCriteriaSetId;
    private CriteriaSetRelationships[] criteriaSetRelationships;
    private CriteriaSetValuePaths[]    criteriaSetValuePaths;

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

    public void setCriteriaSetRelationships(CriteriaSetRelationships[] criteriaSetRelationships) {
        this.criteriaSetRelationships = criteriaSetRelationships;
    }

    public CriteriaSetValuePaths[] getCriteriaSetValuePaths() {
        return criteriaSetValuePaths;
    }

    public void setCriteriaSetValuePaths(CriteriaSetValuePaths[] criteriaSetValuePaths) {
        this.criteriaSetValuePaths = criteriaSetValuePaths;
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
        result = prime * result + ((Name == null) ? 0 : Name.hashCode());
        result = prime * result + ((ParentCriteriaSetId == null) ? 0 : ParentCriteriaSetId.hashCode());
        result = prime * result + ((ProductId == null) ? 0 : ProductId.hashCode());
        result = prime * result + Arrays.hashCode(criteriaSetRelationships);
        result = prime * result + Arrays.hashCode(criteriaSetValuePaths);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Relationships other = (Relationships) obj;
        if (Name == null) {
            if (other.Name != null) {
                return false;
            }
        } else if (!Name.equals(other.Name)) {
            return false;
        }
        if (ParentCriteriaSetId == null) {
            if (other.ParentCriteriaSetId != null) {
                return false;
            }
        } else if (!ParentCriteriaSetId.equals(other.ParentCriteriaSetId)) {
            return false;
        }
        if (ProductId == null) {
            if (other.ProductId != null) {
                return false;
            }
        } else if (!ProductId.equals(other.ProductId)) {
            return false;
        }
        if (!Arrays.equals(criteriaSetRelationships, other.criteriaSetRelationships)) {
            return false;
        }
        if (!Arrays.equals(criteriaSetValuePaths, other.criteriaSetValuePaths)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
