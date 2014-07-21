
package com.asi.service.product.client.vo.origin;

import java.util.HashMap;
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
    "ID",
    "CodeValue",
    "StandardValueFlag"
})
public class Origin {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("CodeValue")
    private String codeValue;
    @JsonProperty("StandardValueFlag")
    private String standardValueFlag;
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(Integer iD) {
        this.iD = iD;
    }

    @JsonProperty("CodeValue")
    public String getCodeValue() {
        return codeValue;
    }

    @JsonProperty("CodeValue")
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @JsonProperty("StandardValueFlag")
    public String getStandardValueFlag() {
        return standardValueFlag;
    }

    @JsonProperty("StandardValueFlag")
    public void setStandardValueFlag(String standardValueFlag) {
        this.standardValueFlag = standardValueFlag;
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
