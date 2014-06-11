package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductKeywords {
	//@JsonProperty("ID")
	private String id="0";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarketSegmentCode() {
		return marketSegmentCode;
	}
	public void setMarketSegmentCode(String marketSegmentCode) {
		this.marketSegmentCode = marketSegmentCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private String marketSegmentCode="";
	private String productId="";
	private String typeCode="";
	private String value="";
}
