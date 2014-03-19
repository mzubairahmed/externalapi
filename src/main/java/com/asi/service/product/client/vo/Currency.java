
package com.asi.service.product.client.vo;

import java.util.HashMap;
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
    "Code",
    "Name",
    "Number",
    "ASIDisplaySymbol",
    "ISODisplaySymbol",
    "IsISO",
    "IsActive",
    "DisplaySequence"
})
public class Currency {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Number")
    private Integer number;
    @JsonProperty("ASIDisplaySymbol")
    private String aSIDisplaySymbol;
    @JsonProperty("ISODisplaySymbol")
    private String iSODisplaySymbol;
    @JsonProperty("IsISO")
    private Boolean isISO;
    @JsonProperty("IsActive")
    private Boolean isActive;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Number")
    public Integer getNumber() {
        return number;
    }

    @JsonProperty("Number")
    public void setNumber(Integer number) {
        this.number = number;
    }

    @JsonProperty("ASIDisplaySymbol")
    public String getASIDisplaySymbol() {
        return aSIDisplaySymbol;
    }

    @JsonProperty("ASIDisplaySymbol")
    public void setASIDisplaySymbol(String aSIDisplaySymbol) {
        this.aSIDisplaySymbol = aSIDisplaySymbol;
    }

    @JsonProperty("ISODisplaySymbol")
    public String getISODisplaySymbol() {
        return iSODisplaySymbol;
    }

    @JsonProperty("ISODisplaySymbol")
    public void setISODisplaySymbol(String iSODisplaySymbol) {
        this.iSODisplaySymbol = iSODisplaySymbol;
    }

    @JsonProperty("IsISO")
    public Boolean getIsISO() {
        return isISO;
    }

    @JsonProperty("IsISO")
    public void setIsISO(Boolean isISO) {
        this.isISO = isISO;
    }

    @JsonProperty("IsActive")
    public Boolean getIsActive() {
        return isActive;
    }

    @JsonProperty("IsActive")
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @JsonProperty("DisplaySequence")
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    @JsonProperty("DisplaySequence")
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
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
