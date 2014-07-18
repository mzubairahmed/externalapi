package com.asi.ext.api.service.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Image {

    @JsonProperty("ImageURL")
    private String imageURL;
    @JsonProperty("Rank")
    private Integer rank;
    @JsonProperty("IsPrimary")
    private Boolean isPrimary;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ImageURL")
    public String getImageURL() {
        return imageURL;
    }

    @JsonProperty("ImageURL")
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @JsonProperty("Rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("Rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonProperty("IsPrimary")
    public Boolean getIsPrimary() {
        return isPrimary;
    }

    @JsonProperty("IsPrimary")
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
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
