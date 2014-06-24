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
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public void setMarketSegmentCode(String marketSegmentCode) {
		this.marketSegmentCode = marketSegmentCode;
	}
}
