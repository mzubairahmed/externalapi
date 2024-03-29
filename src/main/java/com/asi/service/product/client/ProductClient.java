package com.asi.service.product.client;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.product.transformers.JerseyClientPost;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ExternalApiAuthenticationException;
import com.asi.service.product.exception.InvalidProductException;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.resource.response.ExternalAPIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductClient {
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate             restTemplate;

    private String           productSearchUrl;
    
    @SuppressWarnings("unused")
	private JerseyClientPost jerseyClientPost = new JerseyClientPost();
    private static Logger    _LOGGER          = LoggerFactory.getLogger(ProductClient.class);

    public ProductDetail doIt(String authToken, String productID) throws ProductNotFoundException, ExternalApiAuthenticationException {
        return searchProduct(authToken, productID);
    }

    private ProductDetail searchProduct(String authToken, String productID)
            throws ProductNotFoundException, ExternalApiAuthenticationException

    {
        //String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";
        String productSearchUrl = getProductSearchUrl() + "?externalProductId={productID}";

        ProductDetail product = null;
        try {
        	
        	HttpHeaders header = new HttpHeaders();
        	header.add("AuthToken", authToken);
        	header.setContentType(MediaType.APPLICATION_JSON);
        	
            HttpEntity<String> requestEntity = new HttpEntity<String>(header);
            
            _LOGGER.debug("Hiting the RADAR API...");
            
            ResponseEntity<ProductDetail> response = restTemplate.exchange(productSearchUrl, HttpMethod.GET, requestEntity,
                    ProductDetail.class, productID);
            if(response != null && response.getBody() != null) {
            	product = response.getBody();
            }

        } catch (HttpClientErrorException hce) {
            _LOGGER.error("Exception while posting product to Radar API", hce);
            if (hce.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                
                throw new ExternalApiAuthenticationException(hce, productID, hce.getStatusCode());
            } else if (hce.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                ProductNotFoundException exc = new ProductNotFoundException(hce, productID);
                exc.setStackTrace(hce.getStackTrace());
                throw exc;
            } else {
                ProductNotFoundException exc = new ProductNotFoundException(hce, productID);
                exc.setStackTrace(hce.getStackTrace());
                throw hce;
            }
        }  catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            
        }
        return product;
    }

    public ExternalAPIResponse saveProduct(String authToken, ProductDetail product) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            _LOGGER.info("Product Data : " + mapper.writeValueAsString(product));
            
        	HttpHeaders headers = new HttpHeaders();
        	headers.add("AuthToken", authToken);
        	headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ProductDetail> requestEntity = new HttpEntity<>(product, headers);
            
//            ResponseEntity<?> response = restTemplate.postForObject(productSearchUrl, product, ResponseEntity.class);

            ResponseEntity<Object> response = restTemplate.exchange(productSearchUrl, HttpMethod.POST, requestEntity, Object.class);

            _LOGGER.info("Result : " + response);
            return getExternalAPIResponse("Product Saved successfully", HttpStatus.OK, null);
        } catch (HttpClientErrorException hce) {
            _LOGGER.error("Exception while posting product to Radar API", hce);
            return convertExceptionToResponseModel(hce);
        } catch (Exception e) {
            _LOGGER.error("Exception while posting product to Radar API", e);
            return convertExceptionToResponseModel(e);
        }
    }

    public ExternalAPIResponse convertExceptionToResponseModel(Exception e) {

        if (e == null) {
            return getExternalAPIResponse("Bad Request", HttpStatus.BAD_REQUEST, null);
        } else if (e instanceof HttpClientErrorException) {
            if (((HttpClientErrorException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return getExternalAPIResponse("Your session expired or is no longer valid", HttpStatus.UNAUTHORIZED, null);
            } else {
                return getExternalAPIResponse(((HttpClientErrorException) e).getResponseBodyAsString(), HttpStatus.BAD_REQUEST, null);
            }
        } else if (e instanceof ExternalApiAuthenticationException) { 
            if (((ExternalApiAuthenticationException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return getExternalAPIResponse("Your session expired or is no longer valid", HttpStatus.UNAUTHORIZED, null);
            } else {
                return getExternalAPIResponse(((ExternalApiAuthenticationException) e).getMessage(), HttpStatus.BAD_REQUEST, null);
            }
        } else if (e instanceof InvalidProductException) {
            return getExternalAPIResponse(e.getMessage(),
                    HttpStatus.BAD_REQUEST, null);
        } else {
            return getExternalAPIResponse("Unhandled Exception while processing request, failed to process product",
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    private ExternalAPIResponse getExternalAPIResponse(String message, HttpStatus statusCode, Set<String> additionalInfo) {
        ExternalAPIResponse response = new ExternalAPIResponse();
        if (statusCode != null && message != null && message.toLowerCase().startsWith("{\"Message\":\"Not Valid".toLowerCase())) {
            response.setMessage("Product saved successfully but not active");
            response.setStatusCode(HttpStatus.OK);
        } else {
            response.setStatusCode(statusCode);
            response.setMessage(message);
        }
        response.setAdditionalInfo(additionalInfo);

        return response;
    }
    
    /**
     * @return the productSearchUrl
     */
    public String getProductSearchUrl() {
        return productSearchUrl;
    }

    /**
     * @param productSearchUrl
     *            the productSearchUrl to set
     */
    public void setProductSearchUrl(String productSearchUrl) {
        this.productSearchUrl = productSearchUrl;
    }

}
