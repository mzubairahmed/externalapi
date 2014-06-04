package com.asi.service.ws.product.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.asi.service.ws.product.vo.ProductSearchRequest;
import com.asi.service.ws.product.vo.ProductSearchResponse;

@Endpoint
public class ProductEndPoint {

	private ProductSearchWSService productService;
	@Autowired
	public ProductEndPoint(ProductSearchWSService productSearchService)
	{
		this.productService =productSearchService;
	}
	@PayloadRoot(namespace = "http://www.asicentral.com/schema/product", localPart = "ProductSearchRequest")
	public ProductSearchResponse handleSearchRequest(ProductSearchRequest productSearchRequest)
	{
		ProductSearchResponse response= new ProductSearchResponse();
		
		return response;
	}
}
