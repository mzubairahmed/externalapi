package com.asi.service.ws.product.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.asi.core.exception.ErrorMessage;
import com.asi.core.exception.ErrorMessageHandler;
import com.asi.core.repo.product.ProductRepo;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.Product;
import com.asi.service.ws.product.vo.ProductSearchRequest;
import com.asi.service.ws.product.vo.ProductSearchResponse;

@Service
public class ProductSearchWSService implements ProductService{
	private static Logger _LOGGER = LoggerFactory.getLogger(ProductSearchWSService.class);
	@Autowired ProductDetail serviceResponse; 
	@Autowired ProductRepo repository;
	@Autowired ProductClient productClient;
	@Autowired ErrorMessageHandler errorMessageHandler;

	@Override
	public ProductSearchResponse getProduct(ProductSearchRequest request) {
		String companyId;
		String xid;
		ProductSearchResponse response = new ProductSearchResponse();
		companyId = request.getProduct().getCompanyId().toString();
		
		xid = request.getProduct().getExternalProductId();
		if(_LOGGER.isDebugEnabled())
		{
			_LOGGER.debug("WS Service call customer ID " + companyId + " XID " + xid);
		}
		try {
			Product product = repository.getProductPrices(companyId, xid);
			product.setImprints(repository.getProductImprintMethods(companyId, xid));
			response.setProduct(product);
		} catch (ProductNotFoundException e) {
			ErrorMessage errorInfo =errorMessageHandler.prepairError("error.genericerror.id", null, null, HttpStatus.BAD_REQUEST);
			response.setErrorInfo(errorInfo);
			_LOGGER.error("WS Service call customer ID " + companyId + " XID " + xid + " " + e.getMessage());
		}
		return response;
	}
}
