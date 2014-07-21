
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
    "DisplaySequence",
    "SetCodeValues"
})
public class CodeValueGroup {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("DisplaySequence")
    private Integer displaySequence;
    @JsonProperty("SetCodeValues")
    private List<SetCodeValue> setCodeValues = new ArrayList<SetCodeValue>();
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("SetCodeValues")
    public List<SetCodeValue> getSetCodeValues() {
        return setCodeValues;
    }

    @JsonProperty("SetCodeValues")
    public void setSetCodeValues(List<SetCodeValue> setCodeValues) {
        this.setCodeValues = setCodeValues;
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
