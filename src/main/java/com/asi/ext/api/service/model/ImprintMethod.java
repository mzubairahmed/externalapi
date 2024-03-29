package com.asi.ext.api.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class ImprintMethod {

    @JsonProperty("Type")
    private String        type;
    @JsonProperty("Alias")
    private String        alias;
    @JsonProperty("MinimumOrder")
    private MinimumOrder  minimumOrder;
    @JsonProperty("Artwork")
    private List<Artwork> artwork = null;

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



}
