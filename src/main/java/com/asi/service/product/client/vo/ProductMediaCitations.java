package com.asi.service.product.client.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ProductMediaCitations {
	@JsonProperty("ID")
private String id;
	@JsonProperty("ProductId")
private String productId;
public String getProductId() {
	return productId;
}

public void setProductId(String productId) {
	this.productId = productId;
}

public String getMediaCitationId() {
	return mediaCitationId;
}

public void setMediaCitationId(String mediaCitationId) {
	this.mediaCitationId = mediaCitationId;
}

public String getIsInitMediaCitation() {
	return isInitMediaCitation;
}

public void setIsInitMediaCitation(String isInitMediaCitation) {
	this.isInitMediaCitation = isInitMediaCitation;
}

public List<ProductMediaCitationReferences> getProductMediaCitationReferences() {
	return productMediaCitationReferences;
}

public void setProductMediaCitationReferences(
		List<ProductMediaCitationReferences> productMediaCitationReferences) {
	this.productMediaCitationReferences = productMediaCitationReferences;
}
@JsonProperty("MediaCitationId")
private String mediaCitationId;
@JsonProperty("IsInitMediaCitation")
private String isInitMediaCitation;
@JsonProperty("InitMediaCitationId")
private String initMediaCitationId;
public String getInitMediaCitationId() {
	return initMediaCitationId;
}

public void setInitMediaCitationId(String initMediaCitationId) {
	this.initMediaCitationId = initMediaCitationId;
}
@JsonProperty("ProductMediaCitationReferences")
private List<ProductMediaCitationReferences> productMediaCitationReferences;
public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
} 
}
