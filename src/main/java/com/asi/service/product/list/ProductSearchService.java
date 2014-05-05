package com.asi.service.product.list;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.product.ProductRepo;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.Product;

@RestController
@RequestMapping("api")
public class ProductSearchService {
	@Autowired ProductDetail serviceResponse; 
	@Autowired ProductRepo repository;
	private Logger _LOGGER = LoggerFactory.getLogger(ProductSearchService.class);
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> getProduct(HttpEntity<byte[]> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws UnsupportedEncodingException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
		Product productResponse = repository.getProductPrices(companyId, xid);
		return new ResponseEntity<Product>(productResponse, null, HttpStatus.OK);
	}
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}/price/{priceGridId}", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ItemPriceDetail> getPrice(HttpEntity<byte[]> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid,@PathVariable("priceGridId") Integer priceGridId) throws UnsupportedEncodingException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service with priceid");	
		ItemPriceDetail itemPrice = repository.getProductPrices(companyId, xid,priceGridId).getItemPrice().get(0);
	    return new ResponseEntity<ItemPriceDetail>(itemPrice, null, HttpStatus.OK);
	}

}
