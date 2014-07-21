package com.asi.service.ws.product.list;

import com.asi.service.ws.product.vo.ProductSearchRequest;
import com.asi.service.ws.product.vo.ProductSearchResponse;

public interface ProductService {

	ProductSearchResponse getProduct(ProductSearchRequest request);
}
