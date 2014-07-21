package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImprintMethod {

    @JsonProperty("Type")
    private String        type;
    @JsonProperty("Alias")
    private String        alias;
    @JsonProperty("MinimumOrder")
    private MinimumOrder  minimumOrder;
    @JsonProperty("Artwork")
    private List<Artwork> artwork = new ArrayList<Artwork>();

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("Alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("Alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonProperty("MinimumOrder")
    public MinimumOrder getMinimumOrder() {
        return minimumOrder;
    }

    @JsonProperty("MinimumOrder")
    public void setMinimumOrder(MinimumOrder minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    @JsonProperty("Artwork")
    public List<Artwork> getArtwork() {
        return artwork;
    }

    @JsonProperty("Artwork")
    public void setArtwork(List<Artwork> artwork) {
        this.artwork = artwork;
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
