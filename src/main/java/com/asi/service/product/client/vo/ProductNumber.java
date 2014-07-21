package com.asi.service.product.client.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "ProductId",
    "Value",
    "PriceGridId",
    "ProductNumberConfigurations"
})
public class ProductNumber {
	@JsonProperty("ID")
	private Integer id;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Value")
	private String value;
	@JsonProperty("PriceGridId")
	private Integer priceGridId;
	@JsonProperty("ProductNumberConfigurations")
	private List<ProductNumberConfiguration> productNumberConfigurations;
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
	 * @return the productId
	 */
	@JsonProperty("ProductId")
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	@JsonProperty("ProductId")
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the value
	 */
	@JsonProperty("Value")
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	@JsonProperty("Value")
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the priceGridId
	 */
	@JsonProperty("PriceGridId")
	public Integer getPriceGridId() {
		return priceGridId;
	}
	/**
	 * @param priceGridId the priceGridId to set
	 */
	@JsonProperty("PriceGridId")
	public void setPriceGridId(Integer priceGridId) {
		this.priceGridId = priceGridId;
	}
	/**
	 * @return the productNumberConfiguration
	 */
	@JsonProperty("ProductNumberConfigurations")
	public List<ProductNumberConfiguration> getProductNumberConfigurations() {
		return productNumberConfigurations;
	}
	/**
	 * @param productNumberConfiguration the productNumberConfiguration to set
	 */
	@JsonProperty("ProductNumberConfigurations")
	public void setProductNumberConfigurations(
			List<ProductNumberConfiguration> productNumberConfigurations) {
		this.productNumberConfigurations = productNumberConfigurations;
	}
}
