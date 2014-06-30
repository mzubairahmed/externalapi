
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
    "ProductId",
    "ConfigId",
    "IsDefault",
    "ProductCriteriaSets"
})
public class ProductConfiguration {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("ProductId")
    private Integer productId;
    @JsonProperty("IsDefault")
    private Boolean isDefault;
    @JsonProperty("ConfigId")
    private String configId="";
    
    public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	@JsonProperty("ProductCriteriaSets")
    private List<ProductCriteriaSets> productCriteriaSets = new ArrayList<ProductCriteriaSets>();
  //  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public Integer getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(Integer iD) {
        this.iD = iD;
    }

    @JsonProperty("ProductId")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @JsonProperty("IsDefault")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @JsonProperty("IsDefault")
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("ProductCriteriaSets")
    public List<ProductCriteriaSets> getProductCriteriaSets() {
        return productCriteriaSets;
    }

    @JsonProperty("ProductCriteriaSets")
    public void setProductCriteriaSets(List<ProductCriteriaSets> productCriteriaSets) {
        this.productCriteriaSets = productCriteriaSets;
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

 /*   @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
*/
}
