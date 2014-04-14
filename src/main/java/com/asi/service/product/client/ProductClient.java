package com.asi.service.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.asi.service.product.client.vo.ProductDetail;


@Component
public class ProductClient {
	@Autowired  @Qualifier("restTemplate")  RestTemplate restTemplate;
	private String productSearchUrl;



	public ProductDetail doIt(String companyID,Integer productID) {
		return searchProduct(companyID, productID);
	 }
	 
	 private ProductDetail searchProduct(String companyID, Integer productID)
	 {
		 String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";
		
		 ProductDetail product=null;
		 product = restTemplate.getForObject(productSearchUrl,ProductDetail.class,companyID,  productID);
		
		 return product;
	 }
	/**
	 * @return the productSearchUrl
	 */
	public String getProductSearchUrl() {
		return productSearchUrl;
	}
	/**
	 * @param productSearchUrl the productSearchUrl to set
	 */
	public void setProductSearchUrl(String productSearchUrl) {
		this.productSearchUrl = productSearchUrl;
	}
	 
	 
}
