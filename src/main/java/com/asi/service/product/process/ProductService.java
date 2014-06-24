package com.asi.service.product.process;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.exception.ErrorMessage;
import com.asi.core.exception.ExistingProductException;
import com.asi.core.repo.product.ProductRepo;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.Product;

@RestController
@RequestMapping("api")
public class ProductService {
	@Autowired ProductRepo repository;
	private static Logger _LOGGER = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	private MessageSource messageSource;

	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.PUT,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> createProduct(HttpEntity<Product> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws ProductNotFoundException, ExistingProductException  {
		Product productResponse=null;
		Product currentProduct=null;
		
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
			_LOGGER.info("Product Already Exist");	
				currentProduct=repository.getProductPrices(companyId,xid);
				if(null!=currentProduct){
					throw new ExistingProductException(String.valueOf(currentProduct.getID()));
				}
			currentProduct=requestEntity.getBody();
			productResponse = repository.updateProductBasePrices(currentProduct,"update");
		
		return new ResponseEntity<Product>(productResponse, null, HttpStatus.CREATED);
	}
	
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}/price/{priceGridId}", method = RequestMethod.POST,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ItemPriceDetail> createProductPrice(HttpEntity<ItemPriceDetail> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid,@PathVariable("priceGridId") Integer priceGridId) throws UnsupportedEncodingException, ProductNotFoundException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
		ItemPriceDetail itemPriceResponse = requestEntity.getBody();
		return new ResponseEntity<ItemPriceDetail>(itemPriceResponse, null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "{companyid}/pid/{xid}/imprintMethods",method = RequestMethod.POST, headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Imprints> createImprintMethods(@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws UnsupportedEncodingException, ProductNotFoundException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling Imprint Method Service");
		Imprints productResponse = repository.getProductImprintMethods(companyId, xid);
	    return new ResponseEntity<Imprints>(productResponse, null, HttpStatus.CREATED);
	}
	
	
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.POST,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> updateProduct(HttpEntity<Product> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws Exception {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
		Product productResponse = repository.updateProductBasePrices(requestEntity.getBody(),"update");
		return new ResponseEntity<Product>(productResponse, null, HttpStatus.OK);
	}
	
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}/price/{priceGridId}", method = RequestMethod.PUT,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ItemPriceDetail> updateProductPrice(HttpEntity<ItemPriceDetail> requestEntity,@PathVariable("companyid") String companyId, @PathVariable("xid") String xid,@PathVariable("priceGridId") Integer priceGridId) throws UnsupportedEncodingException, ProductNotFoundException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling service");
		ItemPriceDetail productResponse = requestEntity.getBody();
		
		return new ResponseEntity<ItemPriceDetail>(productResponse, null, HttpStatus.OK);
	}
		
	@RequestMapping(value = "{companyid}/pid/{xid}/imprintMethods",method = RequestMethod.PUT, headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Imprints> updateImprintMethods(@PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws UnsupportedEncodingException, ProductNotFoundException {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling Imprint Method Service");
		Imprints productResponse = repository.getProductImprintMethods(companyId, xid);
	    return new ResponseEntity<Imprints>(productResponse, null, HttpStatus.OK);
	}	
	
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "{companyid}/pid/{xid}/basePrices",method = RequestMethod.POST, headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<Product> updateBasePrices(HttpEntity<Product> product) throws Exception {
		if(_LOGGER.isDebugEnabled()) 
			_LOGGER.debug("calling Base Price Service Updation");
		Product productResponse = repository.updateProductBasePrices(product.getBody(),"update");
	    return new ResponseEntity<Product>(productResponse, null, HttpStatus.OK);
	  	}
	
	
	@ExceptionHandler(ProductNotFoundException.class)
	 public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(ProductNotFoundException ex, HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage("error.no.priduct.id", null, locale);
   	errorMessage += " " + ex.getProductID();
       String errorURL = request.getRequestURL().toString();
       ErrorMessage errorInfo = new ErrorMessage();
		errorInfo.setErrorMessage(errorMessage);
		errorInfo.setErrorURL(errorURL);
		errorInfo.setStatusCode(HttpStatus.NOT_FOUND);
		List<String> errorsList = new ArrayList<String>();
		errorsList.add(ex.getMessage());
		errorInfo.setErrors(errorsList);
		_LOGGER.error(errorMessage + errorURL);
		return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.NOT_FOUND);
	}	
	@ExceptionHandler(ExistingProductException.class)
	 public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(ExistingProductException ex, HttpServletRequest request) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage("error.existing.priduct.id", null, locale);
  	errorMessage += " " + ex.getProductID();
      String errorURL = request.getRequestURL().toString();
      ErrorMessage errorInfo = new ErrorMessage();
		errorInfo.setErrorMessage(errorMessage);
		errorInfo.setErrorURL(errorURL);
		errorInfo.setStatusCode(HttpStatus.BAD_REQUEST);
		List<String> errorsList = new ArrayList<String>();
		errorsList.add(ex.getMessage());
		errorInfo.setErrors(errorsList);
		_LOGGER.error(errorMessage + errorURL);
		return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.BAD_REQUEST);
	}	
}
