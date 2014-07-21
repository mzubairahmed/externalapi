
package com.asi.service.product.client.vo.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "Code",
    "Description",
    "DisplayName",
    "IsDefault",
    "DisplaySequence",
    "MajorCodeValueGroupCode",
    "SetCodeValues"
})
public class CodeValueGroup {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("IsDefault")
    private Boolean isDefault;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    @JsonProperty("MajorCodeValueGroupCode")
    private String majorCodeValueGroupCode;
    @JsonProperty("SetCodeValues")
    private List<SetCodeValue> setCodeValues = new ArrayList<SetCodeValue>();
  //  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
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

    @JsonProperty("IsDefault")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @JsonProperty("IsDefault")
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("DisplaySequence")
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    @JsonProperty("DisplaySequence")
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    @JsonProperty("MajorCodeValueGroupCode")
    public String getMajorCodeValueGroupCode() {
        return majorCodeValueGroupCode;
    }

    @JsonProperty("MajorCodeValueGroupCode")
    public void setMajorCodeValueGroupCode(String majorCodeValueGroupCode) {
        this.majorCodeValueGroupCode = majorCodeValueGroupCode;
    }

    @JsonProperty("SetCodeValues")
    public List<SetCodeValue> getSetCodeValues() {
        return setCodeValues;
    }

    @JsonProperty("SetCodeValues")
    public void setSetCodeValues(List<SetCodeValue> setCodeValues) {
        this.setCodeValues = setCodeValues;
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
