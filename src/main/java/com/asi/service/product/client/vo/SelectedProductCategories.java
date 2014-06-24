package com.asi.service.product.client.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class SelectedProductCategories {
	String code="";
	@JsonProperty("AdCategoryFlg")
	String adCategoryFlg="";
	
	public String getAdCategoryFlg() {
		return adCategoryFlg;
	}
	public void setAdCategoryFlg(String adCategoryFlg) {
		this.adCategoryFlg = adCategoryFlg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	String isPrimary="";
	String productId="";


}
