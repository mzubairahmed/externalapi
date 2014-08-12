
package com.asi.service.product.client.vo.category;

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
    "Code",
    "Description",
    "Name",
    "ParentCategoryCode",
    "ProductTypeCode",
    "IsProductTypeSpecific"
})
public class ProductCategory {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("ParentCategoryCode")
    private String parentCategoryCode;
    @JsonProperty("ProductTypeCode")
    private String productTypeCode;
    @JsonProperty("IsProductTypeSpecific")
    private Boolean isProductTypeSpecific;
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

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ParentCategoryCode")
    public String getParentCategoryCode() {
        return parentCategoryCode;
    }

    @JsonProperty("ParentCategoryCode")
    public void setParentCategoryCode(String parentCategoryCode) {
        this.parentCategoryCode = parentCategoryCode;
    }

    @JsonProperty("ProductTypeCode")
    public String getProductTypeCode() {
        return productTypeCode;
    }

    @JsonProperty("ProductTypeCode")
    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    @JsonProperty("IsProductTypeSpecific")
    public Boolean getIsProductTypeSpecific() {
        return isProductTypeSpecific;
    }

    @JsonProperty("IsProductTypeSpecific")
    public void setIsProductTypeSpecific(Boolean isProductTypeSpecific) {
        this.isProductTypeSpecific = isProductTypeSpecific;
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
