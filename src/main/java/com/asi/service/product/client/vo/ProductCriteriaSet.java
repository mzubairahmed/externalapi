
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
    "ProductId",
    "CompanyId",
    "ParentCriteriaSetId",
    "CriteriaSetId",
    "ConfigId",
    "CriteriaCode",
    "Description",
    "OrderDetail",
    "CriteriaDetail",
    "IsBase",
    "IsRequiredForOrder",
    "IsMultipleChoiceAllowed",
    "IsBrokenOutOn",
    "IsTemplate",
    "IsDefaultConfiguration",
    "CriteriaSetValues"
})
public class ProductCriteriaSet {

    @JsonProperty("ProductId")
    private Integer productId;
    @JsonProperty("CompanyId")
    private Integer companyId;
    @JsonProperty("ParentCriteriaSetId")
    private Integer parentCriteriaSetId;
    @JsonProperty("CriteriaSetId")
    private Integer criteriaSetId;
    @JsonProperty("ConfigId")
    private Integer configId;
    @JsonProperty("CriteriaCode")
    private String criteriaCode;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("OrderDetail")
    private String orderDetail;
    @JsonProperty("CriteriaDetail")
    private String criteriaDetail;
    @JsonProperty("IsBase")
    private Boolean isBase;
    @JsonProperty("IsRequiredForOrder")
    private Boolean isRequiredForOrder;
    @JsonProperty("IsMultipleChoiceAllowed")
    private Boolean isMultipleChoiceAllowed;
    @JsonProperty("IsBrokenOutOn")
    private Boolean isBrokenOutOn;
    @JsonProperty("IsTemplate")
    private Boolean isTemplate;
    @JsonProperty("IsDefaultConfiguration")
    private Boolean isDefaultConfiguration;
    @JsonProperty("CriteriaSetValues")
    private List<CriteriaSetValue> criteriaSetValues = new ArrayList<CriteriaSetValue>();
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ProductId")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @JsonProperty("CompanyId")
    public Integer getCompanyId() {
        return companyId;
    }

    @JsonProperty("CompanyId")
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @JsonProperty("ParentCriteriaSetId")
    public Integer getParentCriteriaSetId() {
        return parentCriteriaSetId;
    }

    @JsonProperty("ParentCriteriaSetId")
    public void setParentCriteriaSetId(Integer parentCriteriaSetId) {
        this.parentCriteriaSetId = parentCriteriaSetId;
    }

    @JsonProperty("CriteriaSetId")
    public Integer getCriteriaSetId() {
        return criteriaSetId;
    }

    @JsonProperty("CriteriaSetId")
    public void setCriteriaSetId(Integer criteriaSetId) {
        this.criteriaSetId = criteriaSetId;
    }

    @JsonProperty("ConfigId")
    public Integer getConfigId() {
        return configId;
    }

    @JsonProperty("ConfigId")
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    @JsonProperty("CriteriaCode")
    public String getCriteriaCode() {
        return criteriaCode;
    }

    @JsonProperty("CriteriaCode")
    public void setCriteriaCode(String criteriaCode) {
        this.criteriaCode = criteriaCode;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("OrderDetail")
    public String getOrderDetail() {
        return orderDetail;
    }

    @JsonProperty("OrderDetail")
    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    @JsonProperty("CriteriaDetail")
    public String getCriteriaDetail() {
        return criteriaDetail;
    }

    @JsonProperty("CriteriaDetail")
    public void setCriteriaDetail(String criteriaDetail) {
        this.criteriaDetail = criteriaDetail;
    }

    @JsonProperty("IsBase")
    public Boolean getIsBase() {
        return isBase;
    }

    @JsonProperty("IsBase")
    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    @JsonProperty("IsRequiredForOrder")
    public Boolean getIsRequiredForOrder() {
        return isRequiredForOrder;
    }

    @JsonProperty("IsRequiredForOrder")
    public void setIsRequiredForOrder(Boolean isRequiredForOrder) {
        this.isRequiredForOrder = isRequiredForOrder;
    }

    @JsonProperty("IsMultipleChoiceAllowed")
    public Boolean getIsMultipleChoiceAllowed() {
        return isMultipleChoiceAllowed;
    }

    @JsonProperty("IsMultipleChoiceAllowed")
    public void setIsMultipleChoiceAllowed(Boolean isMultipleChoiceAllowed) {
        this.isMultipleChoiceAllowed = isMultipleChoiceAllowed;
    }

    @JsonProperty("IsBrokenOutOn")
    public Boolean getIsBrokenOutOn() {
        return isBrokenOutOn;
    }

    @JsonProperty("IsBrokenOutOn")
    public void setIsBrokenOutOn(Boolean isBrokenOutOn) {
        this.isBrokenOutOn = isBrokenOutOn;
    }

    @JsonProperty("IsTemplate")
    public Boolean getIsTemplate() {
        return isTemplate;
    }

    @JsonProperty("IsTemplate")
    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    @JsonProperty("IsDefaultConfiguration")
    public Boolean getIsDefaultConfiguration() {
        return isDefaultConfiguration;
    }

    @JsonProperty("IsDefaultConfiguration")
    public void setIsDefaultConfiguration(Boolean isDefaultConfiguration) {
        this.isDefaultConfiguration = isDefaultConfiguration;
    }

    @JsonProperty("CriteriaSetValues")
    public List<CriteriaSetValue> getCriteriaSetValues() {
        return criteriaSetValues;
    }

    @JsonProperty("CriteriaSetValues")
    public void setCriteriaSetValues(List<CriteriaSetValue> criteriaSetValues) {
        this.criteriaSetValues = criteriaSetValues;
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
