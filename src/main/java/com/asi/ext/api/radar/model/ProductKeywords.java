package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductKeywords {
	@JsonProperty("ID")
	private String id = "0";

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

	@JsonProperty("marketSegmentCode")
	private String marketSegmentCode = "";
	@JsonProperty("ProductId")
	private String productId = "";
	@JsonProperty("TypeCode")
	private String typeCode = "";
	@JsonProperty("Value")
	private String value = "";
}
