package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectedSafetyWarnings {
@JsonProperty("Code")
private String code="";
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public String getMarketSegmentCode() {
	return marketSegmentCode;
}
public void setMarketSegmentCode(String marketSegmentCode) {
	this.marketSegmentCode = marketSegmentCode;
}
@JsonProperty("ProductId")
private String productId="";
@JsonProperty("MarketSegmentCode")
private String marketSegmentCode="";
}