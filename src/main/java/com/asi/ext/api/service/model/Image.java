package com.asi.ext.api.service.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.asi.ext.api.radar.model.MediaCriteriaMatches;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    @JsonProperty("ImageURL")
    private String imageURL;
    @JsonProperty("Rank")
    private Integer rank;
    @JsonProperty("IsPrimary")
    private Boolean isPrimary;
    @JsonProperty("Configurations")
    private List<Configurations> configurations;

    public List<Configurations> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(
			List<Configurations> mediaCriteriaMatches) {
		this.configurations = mediaCriteriaMatches;
	}

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

   
}
