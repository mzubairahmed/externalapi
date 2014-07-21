
package com.asi.service.product.client.vo.criteria;

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
    "DisplaySequence",
    "IsAllowBasePrice",
    "IsAllowUpcharge",
    "DefaultPriceGridSubTypeCode",
    "IsFlag",
    "IsProductNumberAssignmentAllowed",
    "IsMediaAssignmentAllowed",
    "CodeValueGroups"
})
public class Criterium {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    @JsonProperty("IsAllowBasePrice")
    private Boolean isAllowBasePrice;
    @JsonProperty("IsAllowUpcharge")
    private Boolean isAllowUpcharge;
    @JsonProperty("DefaultPriceGridSubTypeCode")
    private String defaultPriceGridSubTypeCode;
    @JsonProperty("IsFlag")
    private Boolean isFlag;
    @JsonProperty("IsProductNumberAssignmentAllowed")
    private Boolean isProductNumberAssignmentAllowed;
    @JsonProperty("IsMediaAssignmentAllowed")
    private Boolean isMediaAssignmentAllowed;
    @JsonProperty("CodeValueGroups")
    private List<CodeValueGroup> codeValueGroups = new ArrayList<CodeValueGroup>();
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("DisplaySequence")
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    @JsonProperty("DisplaySequence")
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    @JsonProperty("IsAllowBasePrice")
    public Boolean getIsAllowBasePrice() {
        return isAllowBasePrice;
    }

    @JsonProperty("IsAllowBasePrice")
    public void setIsAllowBasePrice(Boolean isAllowBasePrice) {
        this.isAllowBasePrice = isAllowBasePrice;
    }

    @JsonProperty("IsAllowUpcharge")
    public Boolean getIsAllowUpcharge() {
        return isAllowUpcharge;
    }

    @JsonProperty("IsAllowUpcharge")
    public void setIsAllowUpcharge(Boolean isAllowUpcharge) {
        this.isAllowUpcharge = isAllowUpcharge;
    }

    @JsonProperty("DefaultPriceGridSubTypeCode")
    public String getDefaultPriceGridSubTypeCode() {
        return defaultPriceGridSubTypeCode;
    }

    @JsonProperty("DefaultPriceGridSubTypeCode")
    public void setDefaultPriceGridSubTypeCode(String defaultPriceGridSubTypeCode) {
        this.defaultPriceGridSubTypeCode = defaultPriceGridSubTypeCode;
    }

    @JsonProperty("IsFlag")
    public Boolean getIsFlag() {
        return isFlag;
    }

    @JsonProperty("IsFlag")
    public void setIsFlag(Boolean isFlag) {
        this.isFlag = isFlag;
    }

    @JsonProperty("IsProductNumberAssignmentAllowed")
    public Boolean getIsProductNumberAssignmentAllowed() {
        return isProductNumberAssignmentAllowed;
    }

    @JsonProperty("IsProductNumberAssignmentAllowed")
    public void setIsProductNumberAssignmentAllowed(Boolean isProductNumberAssignmentAllowed) {
        this.isProductNumberAssignmentAllowed = isProductNumberAssignmentAllowed;
    }

    @JsonProperty("IsMediaAssignmentAllowed")
    public Boolean getIsMediaAssignmentAllowed() {
        return isMediaAssignmentAllowed;
    }

    @JsonProperty("IsMediaAssignmentAllowed")
    public void setIsMediaAssignmentAllowed(Boolean isMediaAssignmentAllowed) {
        this.isMediaAssignmentAllowed = isMediaAssignmentAllowed;
    }

    @JsonProperty("CodeValueGroups")
    public List<CodeValueGroup> getCodeValueGroups() {
        return codeValueGroups;
    }

    @JsonProperty("CodeValueGroups")
    public void setCodeValueGroups(List<CodeValueGroup> codeValueGroups) {
        this.codeValueGroups = codeValueGroups;
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
