package com.asi.service.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;


@Component
public class ProductClient {
	@Autowired  @Qualifier("restTemplate")  RestTemplate restTemplate;
	private String productSearchUrl;



	public ProductDetail doIt(String companyID,String productID) throws ProductNotFoundException {
		return searchProduct(companyID, productID);
	 }
	 
	 private ProductDetail searchProduct(String companyID, String productID) throws ProductNotFoundException


	 {
		 String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";
		
		 ProductDetail product=null;
		 try
		 {
			 product = restTemplate.getForObject(productSearchUrl,ProductDetail.class,companyID,  productID);
		
		 } catch(RestClientException ex)
		 {
			 throw new ProductNotFoundException(productID);
		 }
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
