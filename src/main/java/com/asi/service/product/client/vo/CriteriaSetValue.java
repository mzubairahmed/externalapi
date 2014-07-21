
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
    "CriteriaCode",
    "ValueTypeCode",
    "CriteriaValueDetail",
    "IsSubset",
    "IsSetValueMeasurement",
    "BaseLookupValue",
    "Value",
    "FormatValue",
    "CriteriaSetId",
    "CriteriaSetCodeValues"
})
public class CriteriaSetValue {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("CriteriaCode")
    private String criteriaCode;
    @JsonProperty("ValueTypeCode")
    private String valueTypeCode;
    @JsonProperty("CriteriaValueDetail")
    private String criteriaValueDetail;
    @JsonProperty("IsSubset")
    private Boolean isSubset;
    @JsonProperty("IsSetValueMeasurement")
    private Boolean isSetValueMeasurement;
    @JsonProperty("BaseLookupValue")
    private String baseLookupValue;
    @JsonProperty("Value")
    private Object value;
    @JsonProperty("FormatValue")
    private String formatValue;
    @JsonProperty("CriteriaSetId")
    private Integer criteriaSetId;
    @JsonProperty("CriteriaSetCodeValues")
    private List<CriteriaSetCodeValue> criteriaSetCodeValues = new ArrayList<CriteriaSetCodeValue>();
  //  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(Integer iD) {
        this.iD = iD;
    }

    @JsonProperty("CriteriaCode")
    public String getCriteriaCode() {
        return criteriaCode;
    }

    @JsonProperty("CriteriaCode")
    public void setCriteriaCode(String criteriaCode) {
        this.criteriaCode = criteriaCode;
    }

    @JsonProperty("ValueTypeCode")
    public String getValueTypeCode() {
        return valueTypeCode;
    }

    @JsonProperty("ValueTypeCode")
    public void setValueTypeCode(String valueTypeCode) {
        this.valueTypeCode = valueTypeCode;
    }

    @JsonProperty("CriteriaValueDetail")
    public String getCriteriaValueDetail() {
        return criteriaValueDetail;
    }

    @JsonProperty("CriteriaValueDetail")
    public void setCriteriaValueDetail(String criteriaValueDetail) {
        this.criteriaValueDetail = criteriaValueDetail;
    }

    @JsonProperty("IsSubset")
    public Boolean getIsSubset() {
        return isSubset;
    }

    @JsonProperty("IsSubset")
    public void setIsSubset(Boolean isSubset) {
        this.isSubset = isSubset;
    }

    @JsonProperty("IsSetValueMeasurement")
    public Boolean getIsSetValueMeasurement() {
        return isSetValueMeasurement;
    }

    @JsonProperty("IsSetValueMeasurement")
    public void setIsSetValueMeasurement(Boolean isSetValueMeasurement) {
        this.isSetValueMeasurement = isSetValueMeasurement;
    }

    @JsonProperty("BaseLookupValue")
    public String getBaseLookupValue() {
        return baseLookupValue;
    }

    @JsonProperty("BaseLookupValue")
    public void setBaseLookupValue(String baseLookupValue) {
        this.baseLookupValue = baseLookupValue;
    }

    @JsonProperty("Value")
    public Object getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(Object value) {
        this.value = value;
    }

    @JsonProperty("FormatValue")
    public String getFormatValue() {
        return formatValue;
    }

    @JsonProperty("FormatValue")
    public void setFormatValue(String formatValue) {
        this.formatValue = formatValue;
    }

    @JsonProperty("CriteriaSetId")
    public Integer getCriteriaSetId() {
        return criteriaSetId;
    }

    @JsonProperty("CriteriaSetId")
    public void setCriteriaSetId(Integer criteriaSetId) {
        this.criteriaSetId = criteriaSetId;
    }

    @JsonProperty("CriteriaSetCodeValues")
    public List<CriteriaSetCodeValue> getCriteriaSetCodeValues() {
        return criteriaSetCodeValues;
    }

    @JsonProperty("CriteriaSetCodeValues")
    public void setCriteriaSetCodeValues(List<CriteriaSetCodeValue> criteriaSetCodeValues) {
        this.criteriaSetCodeValues = criteriaSetCodeValues;
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

   /* @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
