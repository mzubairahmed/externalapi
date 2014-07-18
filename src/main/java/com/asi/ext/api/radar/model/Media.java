package com.asi.ext.api.radar.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Media {
    @JsonProperty("ID")
    private String                 id               = "0";
    private String                 mediaTypeCode    = "";
    private String                 imageQualityCode = "";
    private String                 url;
    private String                 description;
    @JsonProperty("CompanyID")
    private String                 companyID;
    private MediaCriteriaMatches[] mediaCriteriaMatches;

    public String getId() {
        return id;
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

}
