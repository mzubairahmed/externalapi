package com.asi.service.product.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.exception.VelocityImportExceptionCodes;
import com.asi.ext.api.product.transformers.JerseyClientPost;
import com.asi.service.product.client.vo.ProductDetail;
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

    public ProductDetail doIt(HttpHeaders headers, String companyID, String productID) throws ProductNotFoundException {
        return searchProduct(headers, companyID, productID);
    }

    public ProductDetail getRadarProduct(String companyID, String productID) throws ProductNotFoundException {
        return searchRadarProduct(companyID, productID);
    }

    private ProductDetail searchProduct(final HttpHeaders headers, String companyID, String productID)
            throws ProductNotFoundException

    {
        String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";

        ProductDetail product = null;
        try {

            // List<ClientHttpRequestInterceptor> httpInterceptors = new ArrayList<ClientHttpRequestInterceptor>();
            // ClientHttpRequestInterceptor acceptHeader = new ClientHttpRequestInterceptor() {
            //
            // @Override
            // public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            // ClientHttpRequestExecution execution) throws IOException {
            // HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            // requestWrapper.getHeaders().set("AuthToken", headers.get("AuthToken").toString());
            // return execution.execute(requestWrapper, body);
            // }
            // };
            // httpInterceptors.add(acceptHeader);
            //
            // restTemplate.setInterceptors(httpInterceptors);
            //
            // product = restTemplate.getForObject(productSearchUrl, ProductDetail.class, companyID, productID);

            // headers.remove("accept-encoding");
            HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

            _LOGGER.debug("Hiting the RADAR API...");

            ResponseEntity<ProductDetail> response = restTemplate.exchange(productSearchUrl, HttpMethod.GET, requestEntity,
                    ProductDetail.class, companyID, productID);
            if (response != null && response.getBody() != null) {
                product = response.getBody();
            }

        } catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            ProductNotFoundException exc = new ProductNotFoundException(ex, productID);
            exc.setStackTrace(ex.getStackTrace());
            throw exc;
        }
        return product;
    }

    private ProductDetail searchRadarProduct(String companyID, String productID) throws ProductNotFoundException

    {
        String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";

        ProductDetail product = null;
        try {
            // ProductDetailTest details = restTemplate.getForObject(productSearchUrl, ProductDetailTest.class, companyID,
            // productID);
            product = restTemplate.getForObject(productSearchUrl, ProductDetail.class, companyID, productID);

        } catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            throw new ProductNotFoundException(productID);
        }
        return product;
    }

    public ExternalAPIResponse saveProduct(HttpHeaders headers, ProductDetail product) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            _LOGGER.info("Product Data : " + mapper.writeValueAsString(product));

            HttpEntity<ProductDetail> requestEntity = new HttpEntity<>(product, headers);

            ResponseEntity<?> response = restTemplate.postForObject(productSearchUrl, product, ResponseEntity.class);

            // ResponseEntity<?> response = restTemplate.exchange(productSearchUrl, HttpMethod.POST, requestEntity,
            // ResponseEntity.class);

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
            return getExternalAPIResponse(((HttpClientErrorException) e).getResponseBodyAsString(), HttpStatus.BAD_REQUEST, null);
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
