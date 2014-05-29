package com.asi.service.product.process;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.product.ProductRepo;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.Product;

@RestController
@RequestMapping("api")
public class ProductService {
	@Autowired ProductDetail serviceResponse; 
	@Autowired ProductRepo repository;
	private static Logger _LOGGER = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	private MessageSource messageSource;

	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.PUT,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> updateProduct(HttpEntity<Product> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws UnsupportedEncodingException, ProductNotFoundException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
		Product productResponse = requestEntity.getBody();
		return new ResponseEntity<Product>(productResponse, null, HttpStatus.CREATED);
	}
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}/basePrices",method = RequestMethod.POST, headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> getBasePrices(HttpEntity<Product> product) throws Exception {
		//,HttpEntity<byte[]> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling Base Price Service Updation");
		Product productResponse = repository.updateProductBasePrices(product.getBody());
	    return new ResponseEntity<Product>(productResponse, null, HttpStatus.OK);
	  	}
}
