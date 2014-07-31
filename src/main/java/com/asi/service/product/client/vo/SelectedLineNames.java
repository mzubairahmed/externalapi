package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectedLineNames {
	@JsonProperty("ID")
	private String id;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("MarketSegmentCode")
	private String marketSegmentCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
