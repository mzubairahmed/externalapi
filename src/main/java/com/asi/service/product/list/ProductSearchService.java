package com.asi.service.product.list;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.exception.ErrorMessage;
import com.asi.core.exception.ErrorMessageHandler;
import com.asi.core.repo.product.ProductRepo;
import com.asi.ext.api.service.model.Product;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ExternalApiAuthenticationException;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.exception.UnhandledException;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;

@RestController
@RequestMapping("product")
public class ProductSearchService {
    @Autowired
    ProductDetail       serviceResponse;
    @Autowired
    ProductRepo         repository;
    @Autowired
    ErrorMessageHandler errorMessageHandler;

    @Autowired
    HttpServletRequest  request;

    private static Logger _LOGGER = LoggerFactory.getLogger(ProductSearchService.class);

    // @Secured("ROLE_CUSTOMER")
    @RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.GET, headers = "content-type=application/json, application/xml", produces = {
            "application/xml; charset=UTF-8", "application/json; charset=UTF-8" })
    public ResponseEntity<Product> getProduct(@RequestHeader("AuthToken") String authToken, @PathVariable("companyid") String companyId, @PathVariable("xid") String xid)
            throws UnsupportedEncodingException, ProductNotFoundException, UnhandledException, ExternalApiAuthenticationException {
        if (_LOGGER.isDebugEnabled()) _LOGGER.debug("calling service");
        
        Product productResponse = null;
        if (authToken == null) {
            return new ResponseEntity<Product>(productResponse, null, HttpStatus.UNAUTHORIZED);
        }
        try {

            productResponse = repository.getServiceProduct(authToken, companyId, xid);
        } catch (RuntimeException re) {
        	if(re.getCause() instanceof ExternalApiAuthenticationException) {
        		throw (ExternalApiAuthenticationException) re.getCause();
        	}
        } catch (Exception e) {
            e.printStackTrace();
            // throw new UnhandledException(e.getMessage());
        }
        return new ResponseEntity<Product>(productResponse, null, HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @RequestMapping(value = "{companyid}/pid/{xid}/price/{priceGridId}", method = RequestMethod.GET, headers = "content-type=application/json, application/xml", produces = {
            "application/xml", "application/json" })
    public ResponseEntity<ItemPriceDetail> getPrice(@PathVariable("companyid") String companyId, @PathVariable("xid") String xid,
            @PathVariable("priceGridId") Integer priceGridId) throws UnsupportedEncodingException, ProductNotFoundException {
        if (_LOGGER.isDebugEnabled()) _LOGGER.debug("calling service with priceid");
        // ItemPriceDetail itemPrice = repository.getProductPrices(companyId,
        // xid,priceGridId).getItemPrice().get(0);
        ItemPriceDetail itemPrice = null;
        return new ResponseEntity<ItemPriceDetail>(itemPrice, null, HttpStatus.OK);
    }

    @RequestMapping(value = "{companyid}/pid/{xid}/imprintMethods", method = RequestMethod.GET, headers = "content-type=application/json, application/xml", produces = {
            "application/xml", "application/json" })
    public ResponseEntity<Imprints> getImprintMethods(@PathVariable("companyid") String companyId, @PathVariable("xid") String xid)
            throws UnsupportedEncodingException, ProductNotFoundException {
        if (_LOGGER.isDebugEnabled()) _LOGGER.debug("calling Imprint Method Service");
        // Imprints productResponse =
        // repository.getProductImprintMethods(companyId, xid);
        return null;// new ResponseEntity<Imprints>(productResponse, null,
                    // HttpStatus.OK);
    }
    

    @ExceptionHandler(ExternalApiAuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleIOException(ExternalApiAuthenticationException ex, HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError("error.authentication.id", request, null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessage> handleIOException(IOException ex, HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError("error.genericerror.id", request, null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(UnsupportedEncodingException ex,
            HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError("error.mediaerror.id", request, null,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(ProductNotFoundException ex, HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError(ex, request, null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnhandledException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(UnhandledException ex, HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError(ex, request, null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.NOT_FOUND);
    }
}
