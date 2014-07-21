package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class ProductInventoryLink {
	@JsonProperty("ID")
	private String id="";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@JsonProperty("ProductId")
	private String productId="0";
	@JsonProperty("CompanyId")
	private String companyId="";
	@JsonProperty("Url")
	private String url="";
	
}
