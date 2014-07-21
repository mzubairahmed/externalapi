package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChildCriteriaSetCodeValues {

	@JsonProperty("ParentCriteriaSetCodeValueId")
	private String parentCriteriaSetCodeValueId = "";
	@JsonProperty("ChildCriteriaSetCodeValueId")
	private String childCriteriaSetCodeValueId = "";
	@JsonProperty("ProductId")
	private String productId = "";
	@JsonProperty("ChildCriteriaSetCodeValue")
	private ChildCriteriaSetCodeValue childCriteriaSetCodeValue = null;

	public ChildCriteriaSetCodeValues() {
		// Default constructor
	}

	/**
	 * @param parentCriteriaSetCodeValueId
	 * @param childCriteriaSetCodeValueId
	 * @param productId
	 * @param childCriteriaSetCodeValue
	 */
	public ChildCriteriaSetCodeValues(String parentCriteriaSetCodeValueId,
			String childCriteriaSetCodeValueId, String productId,
			ChildCriteriaSetCodeValue childCriteriaSetCodeValue) {
		this.parentCriteriaSetCodeValueId = parentCriteriaSetCodeValueId;
		this.childCriteriaSetCodeValueId = childCriteriaSetCodeValueId;
		this.productId = productId;
		this.childCriteriaSetCodeValue = childCriteriaSetCodeValue;
	}

	/**
	 * @return the parentCriteriaSetCodeValueId
	 */
	public String getParentCriteriaSetCodeValueId() {
		return parentCriteriaSetCodeValueId;
	}

	/**
	 * @param parentCriteriaSetCodeValueId
	 *            the parentCriteriaSetCodeValueId to set
	 */
	public void setParentCriteriaSetCodeValueId(
			String parentCriteriaSetCodeValueId) {
		this.parentCriteriaSetCodeValueId = parentCriteriaSetCodeValueId;
	}

	/**
	 * @return the childCriteriaSetCodeValueId
	 */
	public String getChildCriteriaSetCodeValueId() {
		return childCriteriaSetCodeValueId;
	}

	/**
	 * @param childCriteriaSetCodeValueId
	 *            the childCriteriaSetCodeValueId to set
	 */
	public void setChildCriteriaSetCodeValueId(
			String childCriteriaSetCodeValueId) {
		this.childCriteriaSetCodeValueId = childCriteriaSetCodeValueId;
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
	 * @return the childCriteriaSetCodeValue
	 */
	public ChildCriteriaSetCodeValue getChildCriteriaSetCodeValue() {
		return childCriteriaSetCodeValue;
	}

	/**
	 * @param childCriteriaSetCodeValue
	 *            the childCriteriaSetCodeValue to set
	 */
	public void setChildCriteriaSetCodeValue(
			ChildCriteriaSetCodeValue childCriteriaSetCodeValue) {
		this.childCriteriaSetCodeValue = childCriteriaSetCodeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChildCriteriaSetCodeValues [parentCriteriaSetCodeValueId="
				+ parentCriteriaSetCodeValueId
				+ ", childCriteriaSetCodeValueId="
				+ childCriteriaSetCodeValueId + ", productId=" + productId
				+ ", childCriteriaSetCodeValue=" + childCriteriaSetCodeValue
				+ "]";
	}

}
