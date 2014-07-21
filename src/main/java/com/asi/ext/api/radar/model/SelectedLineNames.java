package com.asi.ext.api.radar.model;

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

	public SelectedLineNames() {
	}

	/**
	 * @param id
	 * @param name
	 * @param productId
	 * @param marketSegmentCode
	 */
	public SelectedLineNames(String id, String name, String productId,
			String marketSegmentCode) {
		this.id = id;
		this.name = name;
		this.productId = productId;
		this.marketSegmentCode = marketSegmentCode;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the marketSegmentCode
	 */
	public String getMarketSegmentCode() {
		return marketSegmentCode;
	}

	/**
	 * @param marketSegmentCode
	 *            the marketSegmentCode to set
	 */
	public void setMarketSegmentCode(String marketSegmentCode) {
		this.marketSegmentCode = marketSegmentCode;
	}

}
