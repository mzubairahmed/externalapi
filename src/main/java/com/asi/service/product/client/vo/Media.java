package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
	@JsonProperty("ID")
	private String id;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("MediaTypeCode")
	private String mediaTypeCode;
	@JsonProperty("ImageQualityCode")
	private String imageQualityCode;
	@JsonProperty("IsProcessedFlg")
	private String isProcessedFlg;
	@JsonProperty("CompanyID")
	private String companyId;
	@JsonProperty("MediaCriteriaMatches")
	private MediaCriteriaMatches[] mediaCriteriaMatches;
	@JsonProperty("Url")
	private String url;

public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMediaTypeCode() {
		return mediaTypeCode;
	}

	public void setMediaTypeCode(String mediaTypeCode) {
		this.mediaTypeCode = mediaTypeCode;
	}

	public String getImageQualityCode() {
		return imageQualityCode;
	}

	public void setImageQualityCode(String imageQualityCode) {
		this.imageQualityCode = imageQualityCode;
	}

	public String getIsProcessedFlg() {
		return isProcessedFlg;
	}

	public void setIsProcessedFlg(String isProcessedFlg) {
		this.isProcessedFlg = isProcessedFlg;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public MediaCriteriaMatches[] getMediaCriteriaMatches() {
		return mediaCriteriaMatches;
	}

	public void setMediaCriteriaMatches(MediaCriteriaMatches[] mediaCriteriaMatches) {
		this.mediaCriteriaMatches = mediaCriteriaMatches;
	}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

}
