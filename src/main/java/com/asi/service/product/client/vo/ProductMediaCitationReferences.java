package com.asi.service.product.client.vo;


import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductMediaCitationReferences {

    private String                 productId;
    private String                 mediaCitationReferenceId;
    private String                 mediaCitationId;
    private String                 isPrimary;
    @JsonProperty("MediaCitationReference")
    private MediaCitationReference mediaCitationReference;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMediaCitationReferenceId() {
        return mediaCitationReferenceId;
    }

    public void setMediaCitationReferenceId(String mediaCitationReferenceId) {
        this.mediaCitationReferenceId = mediaCitationReferenceId;
    }

    public String getMediaCitationId() {
        return mediaCitationId;
    }

    public void setMediaCitationId(String mediaCitationId) {
        this.mediaCitationId = mediaCitationId;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * @return the mediaCitationReference
     */
    public MediaCitationReference getMediaCitationReference() {
        return mediaCitationReference;
    }

    /**
     * @param mediaCitationReference
     *            the mediaCitationReference to set
     */
    public void setMediaCitationReference(MediaCitationReference mediaCitationReference) {
        this.mediaCitationReference = mediaCitationReference;
    }

}
