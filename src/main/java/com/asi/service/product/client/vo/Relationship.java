
package com.asi.service.product.client.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "Name",
    "ProductId",
    "ParentCriteriaSetId",
    "CriteriaSetRelationships",
    "CriteriaSetValuePaths"
})
public class Relationship {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("ProductId")
    private Integer productId;
    @JsonProperty("ParentCriteriaSetId")
    private Integer parentCriteriaSetId;
    @JsonProperty("CriteriaSetRelationships")
    private List<CriteriaSetRelationship> criteriaSetRelationships = new ArrayList<CriteriaSetRelationship>();
    @JsonProperty("CriteriaSetValuePaths")
    private List<CriteriaSetValuePath> criteriaSetValuePaths = new ArrayList<CriteriaSetValuePath>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(Integer iD) {
        this.iD = iD;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ProductId")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @JsonProperty("ParentCriteriaSetId")
    public Integer getParentCriteriaSetId() {
        return parentCriteriaSetId;
    }

    @JsonProperty("ParentCriteriaSetId")
    public void setParentCriteriaSetId(Integer parentCriteriaSetId) {
        this.parentCriteriaSetId = parentCriteriaSetId;
    }

    @JsonProperty("CriteriaSetRelationships")
    public List<CriteriaSetRelationship> getCriteriaSetRelationships() {
        return criteriaSetRelationships;
    }

    @JsonProperty("CriteriaSetRelationships")
    public void setCriteriaSetRelationships(List<CriteriaSetRelationship> criteriaSetRelationships) {
        this.criteriaSetRelationships = criteriaSetRelationships;
    }

    @JsonProperty("CriteriaSetValuePaths")
    public List<CriteriaSetValuePath> getCriteriaSetValuePaths() {
        return criteriaSetValuePaths;
    }

    @JsonProperty("CriteriaSetValuePaths")
    public void setCriteriaSetValuePaths(List<CriteriaSetValuePath> criteriaSetValuePaths) {
        this.criteriaSetValuePaths = criteriaSetValuePaths;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
