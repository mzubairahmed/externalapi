package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Media {
  @JsonProperty("ID")
  private String id="0";
  public String getId() {
	return id;
}
  @JsonProperty("IsProcessedFlg")
  private String isProcessedFlg;
public String getIsProcessedFlg() {
	return isProcessedFlg;
}
public void setIsProcessedFlg(String isProcessedFlg) {
	this.isProcessedFlg = isProcessedFlg;
}
public void setId(String id) {
	this.id = id;
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
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}

public String getCompanyID() {
    return companyID;
}
public void setCompanyID(String companyID) {
    this.companyID = companyID;
}
@JsonProperty("MediaTypeCode")
private String mediaTypeCode="";
@JsonProperty("ImageQualityCode")
	private String imageQualityCode="";
@JsonProperty("Url")
	private String url;
@JsonProperty("Description")
	private String description;
@JsonProperty("CompanyID")
    private String companyID;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MediaCriteriaMatches[] getMediaCriteriaMatches() {
		return mediaCriteriaMatches;
	}
	public void setMediaCriteriaMatches(MediaCriteriaMatches[] mediaCriteriaMatches) {
		this.mediaCriteriaMatches = mediaCriteriaMatches;
	}
	@JsonProperty("MediaCriteriaMatches")
	private MediaCriteriaMatches[] mediaCriteriaMatches;
   
}
