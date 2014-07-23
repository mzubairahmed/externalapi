package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductMediaItems {
    @JsonProperty("ProductId")
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @JsonProperty("MediaId")
    private String  mediaId;
    @JsonProperty("IsPrimary")
    private Boolean isPrimary;
    @JsonProperty("Media")
    private Media   media;
    @JsonProperty("MediaRank")
    private Integer mediaRank;

    public Integer getMediaRank() {
        return mediaRank;
    }

    public void setMediaRank(Integer mediaRank) {
        this.mediaRank = mediaRank;
    }

}
