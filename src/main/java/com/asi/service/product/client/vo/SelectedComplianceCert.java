
package com.asi.service.product.client.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "ProductId",
    "ComplianceCertId",
    "Description",
    "CompanyId"
})
public class SelectedComplianceCert {

    @JsonProperty("ID")
    private String iD;
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("ComplianceCertId")
    private String complianceCertId;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("CompanyId")
    private String companyId;
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ID")
    public String getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(String iD) {
        this.iD = iD;
    }

    @JsonProperty("ProductId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("ComplianceCertId")
    public String getComplianceCertId() {
        return complianceCertId;
    }

    @JsonProperty("ComplianceCertId")
    public void setComplianceCertId(String complianceCertId) {
        this.complianceCertId = complianceCertId;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("CompanyId")
    public String getCompanyId() {
        return companyId;
    }

    @JsonProperty("CompanyId")
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

  /*  @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
