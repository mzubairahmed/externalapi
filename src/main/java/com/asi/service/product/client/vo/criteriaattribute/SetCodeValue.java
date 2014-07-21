
package com.asi.service.product.client.vo.criteriaattribute;

import java.util.HashMap;
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
    "ID",
    "CodeValue",
    "DisplaySequence",
    "IsSupplierSpecific"
})
public class SetCodeValue {

    @JsonProperty("ID")
    private Integer iD;
    @JsonProperty("CodeValue")
    private String codeValue;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    @JsonProperty("IsSupplierSpecific")
    private Boolean isSupplierSpecific;
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("DisplaySequence")
    public Integer getDisplaySequence() {
        return displaySequence;
    }

    @JsonProperty("DisplaySequence")
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    @JsonProperty("IsSupplierSpecific")
    public Boolean getIsSupplierSpecific() {
        return isSupplierSpecific;
    }

    @JsonProperty("IsSupplierSpecific")
    public void setIsSupplierSpecific(Boolean isSupplierSpecific) {
        this.isSupplierSpecific = isSupplierSpecific;
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
