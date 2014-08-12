
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
    "Code",
    "Description",
    "DisplayName",
    "CriteriaLevelCode",
    "CriteriaGroupCode",
    "CriteriaTypeCode",
    "IsAllowBasePrice",
    "IsAllowUpcharge",
    "IsFlag",
    "IsProductNumberAssignmentAllowed",
    "IsMediaAssignmentAllowed",
    "CodeValueGroups"
})
public class CriteriaItem {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("CriteriaLevelCode")
    private String criteriaLevelCode;
    @JsonProperty("CriteriaGroupCode")
    private String criteriaGroupCode;
    @JsonProperty("CriteriaTypeCode")
    private String criteriaTypeCode;
    @JsonProperty("IsAllowBasePrice")
    private Boolean isAllowBasePrice;
    @JsonProperty("IsAllowUpcharge")
    private Boolean isAllowUpcharge;
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

    @JsonProperty("CriteriaLevelCode")
    public String getCriteriaLevelCode() {
        return criteriaLevelCode;
    }

    @JsonProperty("CriteriaLevelCode")
    public void setCriteriaLevelCode(String criteriaLevelCode) {
        this.criteriaLevelCode = criteriaLevelCode;
    }

    @JsonProperty("CriteriaGroupCode")
    public String getCriteriaGroupCode() {
        return criteriaGroupCode;
    }

    @JsonProperty("CriteriaGroupCode")
    public void setCriteriaGroupCode(String criteriaGroupCode) {
        this.criteriaGroupCode = criteriaGroupCode;
    }

    @JsonProperty("CriteriaTypeCode")
    public String getCriteriaTypeCode() {
        return criteriaTypeCode;
    }

    @JsonProperty("CriteriaTypeCode")
    public void setCriteriaTypeCode(String criteriaTypeCode) {
        this.criteriaTypeCode = criteriaTypeCode;
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

   /* @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
