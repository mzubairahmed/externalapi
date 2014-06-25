package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductMediaCitationReferences {
	@JsonProperty("ProductId")
private String productId;
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
@JsonProperty("MediaCitationReferenceId")
private String mediaCitationReferenceId;
@JsonProperty("MediaCitationId")
private String mediaCitationId;
@JsonProperty("IsPrimary")
private String isPrimary;
}
