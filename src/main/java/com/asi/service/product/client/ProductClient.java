package com.asi.service.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProductClient {
	@Autowired  @Qualifier("restTemplate")  RestTemplate restTemplate;
	private String productSearchUrl;
	private static Logger _LOGGER = LoggerFactory.getLogger(ProductClient.class);


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
			 _LOGGER.error(ex.getMessage());
			 ProductNotFoundException exc = new ProductNotFoundException(ex.getMessage(), ex.getCause());
			 exc.setProductID(productID);
			 throw exc;
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
