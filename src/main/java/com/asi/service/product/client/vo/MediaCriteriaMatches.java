package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaCriteriaMatches {

	@JsonProperty("CriteriaSetValueId")
	private String criteriaSetValueId;
	
	@JsonProperty("MediaId")
	private String mediaId;

	public String getCriteriaSetValueId() {
		return criteriaSetValueId;
	}

	public void setCriteriaSetValueId(String criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	
}
