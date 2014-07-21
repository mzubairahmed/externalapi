package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectedSafetyWarnings {
	@JsonProperty("Code")
	private String code = "";
	@JsonProperty("ProductId")
	private String productId = "";
	@JsonProperty("MarketSegmentCode")
	private String marketSegmentCode = "";

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SelectedSafetyWarnings [code=" + code + ", productId="
				+ productId + ", marketSegmentCode=" + marketSegmentCode + "]";
	}
}
