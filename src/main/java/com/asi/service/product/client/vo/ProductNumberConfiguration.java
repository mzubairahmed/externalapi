package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "ProductNumberId",
    "Value",
    "CriteriaSetValueId",
})
public class ProductNumberConfiguration {
	@JsonProperty("ID")
	private Integer id;
	@JsonProperty("ProductNumberId")
	private String productNumberId;
	@JsonProperty("CriteriaSetValueId")
	private Integer criteriaSetValueId;
	/**
	 * @return the id
	 */
	@JsonProperty("ID")
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	@JsonProperty("ID")
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the productNumberId
	 */
	@JsonProperty("ProductNumberId")
	public String getProductNumberId() {
		return productNumberId;
	}
	/**
	 * @param productNumberId the productNumberId to set
	 */
	@JsonProperty("ProductNumberId")
	public void setProductNumberId(String productNumberId) {
		this.productNumberId = productNumberId;
	}
	/**
	 * @return the criteriaSetValueId
	 */
	@JsonProperty("CriteriaSetValueId")
	public Integer getCriteriaSetValueId() {
		return criteriaSetValueId;
	}
	/**
	 * @param criteriaSetValueId the criteriaSetValueId to set
	 */
	@JsonProperty("CriteriaSetValueId")
	public void setCriteriaSetValueId(Integer criteriaSetValueId) {
		this.criteriaSetValueId = criteriaSetValueId;
	}
}
