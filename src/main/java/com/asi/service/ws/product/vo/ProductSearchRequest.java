package com.asi.service.ws.product.vo;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.product.vo.Product;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="productSearchRequest")
public class ProductSearchRequest {
	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
