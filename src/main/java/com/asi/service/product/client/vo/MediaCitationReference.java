package com.asi.service.product.client.vo;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Shravan B
 * 
 */
public class MediaCitationReference {

    @JsonProperty("ID")
    private String id;
    @JsonProperty("Number")
    private String number;
    @JsonProperty("Sequence")
    private String sequence;
    @JsonProperty("MediaCitationId")
    private String mediaCitationId;
    @JsonProperty("MediaId")
    private String mediaId;

    public MediaCitationReference() {
    }

    /**
     * @param id
     * @param number
     * @param sequence
     * @param mediaCitationId
     */
    public MediaCitationReference(String id, String number, String sequence, String mediaCitationId) {
        this.id = id;
        this.number = number;
        this.sequence = sequence;
        this.mediaCitationId = mediaCitationId;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the mediaCitationId
     */
    public String getMediaCitationId() {
        return mediaCitationId;
    }

    /**
     * @param mediaCitationId
     *            the mediaCitationId to set
     */
    public void setMediaCitationId(String mediaCitationId) {
        this.mediaCitationId = mediaCitationId;
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

