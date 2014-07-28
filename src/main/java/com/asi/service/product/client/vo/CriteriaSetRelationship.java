package com.asi.service.product.client.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "RelationshipId", "CriteriaSetId", "ProductId", "IsParent" })
public class CriteriaSetRelationship {

    @JsonProperty("RelationshipId")
    private Integer relationshipId;
    @JsonProperty("CriteriaSetId")
    private Integer criteriaSetId;
    @JsonProperty("ProductId")
    private Integer productId;
    @JsonProperty("IsParent")
    private Boolean isParent;

    // private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonProperty("RelationshipId")
    public Integer getRelationshipId() {
        return relationshipId;
    }

    @JsonProperty("RelationshipId")
    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    @JsonProperty("CriteriaSetId")
    public Integer getCriteriaSetId() {
        return criteriaSetId;
    }

    @JsonProperty("CriteriaSetId")
    public void setCriteriaSetId(Integer criteriaSetId) {
        this.criteriaSetId = criteriaSetId;
    }

    @JsonProperty("ProductId")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @JsonProperty("IsParent")
    public Boolean getIsParent() {
        return isParent;
    }

    @JsonProperty("IsParent")
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    /*
     * @JsonAnyGetter
     * public Map<String, Object> getAdditionalProperties() {
     * return this.additionalProperties;
     * }
     * 
     * @JsonAnySetter
     * public void setAdditionalProperty(String name, Object value) {
     * this.additionalProperties.put(name, value);
     * }
     */

}
