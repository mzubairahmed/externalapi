
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
    "Description",
    "DiscountPercent",
    "IndustryDiscountCode"
})
public class DiscountRate {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DiscountPercent")
    private Double discountPercent;
    @JsonProperty("IndustryDiscountCode")
    private String industryDiscountCode;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("DiscountPercent")
    public Double getDiscountPercent() {
        return discountPercent;
    }

    @JsonProperty("DiscountPercent")
    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @JsonProperty("IndustryDiscountCode")
    public String getIndustryDiscountCode() {
        return industryDiscountCode;
    }

    @JsonProperty("IndustryDiscountCode")
    public void setIndustryDiscountCode(String industryDiscountCode) {
        this.industryDiscountCode = industryDiscountCode;
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
