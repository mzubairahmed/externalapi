package com.asi.ext.api.radar.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class MediaCriteriaMatches {

    @JsonProperty("CriteriaSetValueId")
    private String criteriaSetValueId;
    @JsonProperty("MediaId")
    private String mediaId;
    /**
     * @return the criteriaSetValueId
     */
    public String getCriteriaSetValueId() {
        return criteriaSetValueId;
    }
    /**
     * @param criteriaSetValueId the criteriaSetValueId to set
     */
    public void setCriteriaSetValueId(String criteriaSetValueId) {
        this.criteriaSetValueId = criteriaSetValueId;
    }
    /**
     * @return the mediaId
     */
    public String getMediaId() {
        return mediaId;
    }
    /**
     * @param mediaId the mediaId to set
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
    
}
