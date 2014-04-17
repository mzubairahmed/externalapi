
package com.asi.service.product.client.vo.criteriaattribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "ID",
    "Description",
    "DisplayName",
    "CriteriaCode",
    "CriteriaItem",
    "UnitsOfMeasure"
})
public class CriteriaAttribute {

    @JsonProperty("ID")
    private Integer ID;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("CriteriaCode")
    private String criteriaCode;
    @JsonProperty("CriteriaItem")
    private CriteriaItem criteriaItem;
    @JsonProperty("UnitsOfMeasure")
    private List<UnitsOfMeasure> unitsOfMeasure = new ArrayList<UnitsOfMeasure>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return ID;
    }

    @JsonProperty("ID")
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("DisplayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("DisplayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("CriteriaCode")
    public String getCriteriaCode() {
        return criteriaCode;
    }

    @JsonProperty("CriteriaCode")
    public void setCriteriaCode(String criteriaCode) {
        this.criteriaCode = criteriaCode;
    }

    @JsonProperty("CriteriaItem")
    public CriteriaItem getCriteriaItem() {
        return criteriaItem;
    }

    @JsonProperty("CriteriaItem")
    public void setCriteriaItem(CriteriaItem criteriaItem) {
        this.criteriaItem = criteriaItem;
    }

    @JsonProperty("UnitsOfMeasure")
    public List<UnitsOfMeasure> getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    @JsonProperty("UnitsOfMeasure")
    public void setUnitsOfMeasure(List<UnitsOfMeasure> unitsOfMeasure) {
        this.unitsOfMeasure = unitsOfMeasure;
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
