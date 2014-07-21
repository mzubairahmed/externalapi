
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
    "Code",
    "Description",
    "DisplayName",
    "Format",
    "ConversionTargetUOMCode"
})
public class UnitsOfMeasure {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Format")
    private String format;
    @JsonProperty("ConversionTargetUOMCode")
    private String conversionTargetUOMCode;
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

    @JsonProperty("Format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("Format")
    public void setFormat(String format) {
        this.format = format;
    }

    @JsonProperty("ConversionTargetUOMCode")
    public String getConversionTargetUOMCode() {
        return conversionTargetUOMCode;
    }

    @JsonProperty("ConversionTargetUOMCode")
    public void setConversionTargetUOMCode(String conversionTargetUOMCode) {
        this.conversionTargetUOMCode = conversionTargetUOMCode;
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
