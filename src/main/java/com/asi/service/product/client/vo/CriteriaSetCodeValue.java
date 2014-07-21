
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
    "CriteriaSetValueId",
    "SetCodeValueId",
    "CodeValue",
    "CodeValueDetail",
    "ChildCriteriaSetCodeValues"
})
public class CriteriaSetCodeValue {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("CriteriaSetValueId")
    private Integer criteriaSetValueId;
    @JsonProperty("SetCodeValueId")
    private Integer setCodeValueId;
    @JsonProperty("CodeValue")
    private String codeValue;
    @JsonProperty("CodeValueDetail")
    private String codeValueDetail;
    @JsonProperty("ChildCriteriaSetCodeValues")
    private List<Object> childCriteriaSetCodeValues = new ArrayList<Object>();
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(Integer iD) {
        this.iD = iD;
    }

    @JsonProperty("CriteriaSetValueId")
    public Integer getCriteriaSetValueId() {
        return criteriaSetValueId;
    }

    @JsonProperty("CriteriaSetValueId")
    public void setCriteriaSetValueId(Integer criteriaSetValueId) {
        this.criteriaSetValueId = criteriaSetValueId;
    }

    @JsonProperty("SetCodeValueId")
    public Integer getSetCodeValueId() {
        return setCodeValueId;
    }

    @JsonProperty("SetCodeValueId")
    public void setSetCodeValueId(Integer setCodeValueId) {
        this.setCodeValueId = setCodeValueId;
    }

    @JsonProperty("CodeValue")
    public String getCodeValue() {
        return codeValue;
    }

    @JsonProperty("CodeValue")
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @JsonProperty("CodeValueDetail")
    public String getCodeValueDetail() {
        return codeValueDetail;
    }

    @JsonProperty("CodeValueDetail")
    public void setCodeValueDetail(String codeValueDetail) {
        this.codeValueDetail = codeValueDetail;
    }

    @JsonProperty("ChildCriteriaSetCodeValues")
    public List<Object> getChildCriteriaSetCodeValues() {
        return childCriteriaSetCodeValues;
    }

    @JsonProperty("ChildCriteriaSetCodeValues")
    public void setChildCriteriaSetCodeValues(List<Object> childCriteriaSetCodeValues) {
        this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
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

    /*@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
