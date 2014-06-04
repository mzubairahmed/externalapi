package com.asi.service.ws.product.vo;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.core.exception.ErrorMessage;
import com.asi.service.product.vo.Product;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="productSearchResponse")
public class ProductSearchResponse {
	private ErrorMessage errorInfo;
	private Product product;
	public ErrorMessage getErrorInfo() {
		return errorInfo;
	}
	public Product getProduct() {
		return product;
	}
	public void setErrorInfo(ErrorMessage errorInfo) {
		this.errorInfo = errorInfo;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	
}
