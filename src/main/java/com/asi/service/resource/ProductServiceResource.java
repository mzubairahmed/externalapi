package com.asi.service.resource;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.asi.core.exception.ErrorMessage;
import com.asi.core.exception.ExistingProductException;
import com.asi.core.exception.ResponseNotValidException;
import com.asi.ext.api.service.ProductService;
import com.asi.ext.api.service.model.Product;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.resource.response.ExternalAPIResponse;

@RestController
@RequestMapping("api")
public class ProductServiceResource {

    @Autowired
    ProductService                   productService;

    private static Logger            _LOGGER = LoggerFactory.getLogger(ProductServiceResource.class);
    @Autowired
    private MessageSource            messageSource;

    @Secured("ROLE_CUSTOMER")
    @RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.PUT, headers = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_ATOM_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_ATOM_XML_VALUE })
    public ResponseEntity<Product> createOrUpdateProduct(HttpEntity<Product> requestEntity, 
            @PathVariable("companyid") String companyId, @PathVariable("xid") String xid) throws ProductNotFoundException,
            ExistingProductException, ResponseNotValidException, RestClientException, UnsupportedEncodingException {
        Product product = null;

        if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug("calling service");
        }
        try {
            // product = productService.updateProduct(companyId, xid, requestEntity.getBody());
        } catch (Exception e) {

        }

        return new ResponseEntity<Product>(product, null, HttpStatus.CREATED);
    }

    // @Secured("ROLE_CUSTOMER")
    @RequestMapping(value = "{companyid}/pid/{xid}", method = RequestMethod.POST, headers = "content-type=application/json, application/xml", produces = {
            "application/xml", "application/json" })
    public ResponseEntity<ExternalAPIResponse> updateProduct(HttpEntity<Product> requestEntity, @RequestHeader("AuthToken") String authToken, @PathVariable("companyid") String companyId,
            @PathVariable("xid") String xid) throws Exception {
        if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug("calling service");
        }
        ExternalAPIResponse message = null;
        if (authToken == null) {
            return new ResponseEntity<ExternalAPIResponse>(message, null, HttpStatus.UNAUTHORIZED);
        } else if (requestEntity == null || requestEntity.getBody() == null) {
            message = new ExternalAPIResponse();
            message.setStatusCode(HttpStatus.BAD_REQUEST);
            message.setMessage("Invalid request, request body can't be null");
            return new ResponseEntity<ExternalAPIResponse>(message, null, HttpStatus.BAD_REQUEST);
        } else if (requestEntity.getBody().getExternalProductId() == null || requestEntity.getBody().getExternalProductId().isEmpty()) {
            // External ProductID required
            message = new ExternalAPIResponse();
            message.setMessage("Invalid request, ExternalProductId can't be null/empty");
            message.setStatusCode(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<ExternalAPIResponse>(message, null, HttpStatus.BAD_REQUEST);
        } else if (!requestEntity.getBody().getExternalProductId().trim().equalsIgnoreCase(xid.trim())) {
            // ExternalProductId in the product is not matched with path param
            message = new ExternalAPIResponse();
            message.setStatusCode(HttpStatus.BAD_REQUEST);
            message.setMessage("Invalid request, ExternalProductId provided in the Product is not matching with path");
            return new ResponseEntity<ExternalAPIResponse>(message, null, HttpStatus.BAD_REQUEST);
        }
        try {
            message = productService.updateProduct(authToken, companyId, xid, requestEntity.getBody());
        } catch (Exception e) {
            throw e;
        }

        return new ResponseEntity<ExternalAPIResponse>(message, null, message.getStatusCode());
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

    @ExceptionHandler(ResponseNotValidException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedEncodingException(ResponseNotValidException ex, HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage("error.notvalid.response.id", null, locale);
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

    /**
     * @return the productService
     */
    public ProductService getProductService() {
        return productService;
    }

    /**
     * @param productService
     *            the productService to set
     */
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

}
