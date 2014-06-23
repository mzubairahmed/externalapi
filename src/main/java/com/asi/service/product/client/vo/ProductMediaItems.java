package com.asi.service.product.client.vo;



public class ProductMediaItems {
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
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public Media getMedia() {
		return media;
	}
	public void setMedia(Media media) {
		this.media = media;
	}
	private String mediaId;
	private String isPrimary;
	private Media media;
	private String mediaRank;
	  
 
      public String getMediaRank() {
		return mediaRank;
	}
	public void setMediaRank(String mediaRank) {
		this.mediaRank = mediaRank;
	}
    

	
   
}
