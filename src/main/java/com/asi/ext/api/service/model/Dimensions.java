package com.asi.ext.api.service.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dimensions {

    @JsonProperty("Length")
    private String length;
    @JsonProperty("Width")
    private String width;
    @JsonProperty("Height")
    private String height;

    @JsonProperty("Length")
    public String getLength() {
        return length;
    }

    @JsonProperty("Length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("Width")
    public String getWidth() {
        return width;
    }

    @JsonProperty("Width")
    public void setWidth(String width) {
        this.width = width;
    }

    @JsonProperty("Height")
    public String getHeight() {
        return height;
    }

    @JsonProperty("Height")
    public void setHeight(String height) {
        this.height = height;
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

}
