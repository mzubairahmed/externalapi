package com.asi.service.product.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.product.transformers.JerseyClientPost;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductDetailTest;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductClient {
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate             restTemplate;
    private String           productSearchUrl;
    private JerseyClientPost jerseyClientPost = new JerseyClientPost();
    private static Logger    _LOGGER          = LoggerFactory.getLogger(ProductClient.class);

    public ProductDetail doIt(String companyID, String productID) throws ProductNotFoundException {
        return searchProduct(companyID, productID);
    }

    public ProductDetail getRadarProduct(String companyID, String productID) throws ProductNotFoundException {
        return searchRadarProduct(companyID, productID);
    }

    private ProductDetail searchProduct(String companyID, String productID) throws ProductNotFoundException

    {
        String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";

        ProductDetail product = null;
        try {
            product = restTemplate.getForObject(productSearchUrl, ProductDetail.class, companyID, productID);

        } catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            ProductNotFoundException exc = new ProductNotFoundException(productID);
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

            System.out.println(productID);

        } catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            throw new ProductNotFoundException(productID);
        }
        return product;
    }

    public String saveProduct(ProductDetail product) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            _LOGGER.info("Product Data : "+mapper.writeValueAsString(product));
            String response = restTemplate.postForObject(productSearchUrl, product, String.class);
            /*
             * ObjectMapper mapper = new ObjectMapper();
             * String productData = mapper.writeValueAsString(product);
             * _LOGGER.info("Product Data  : " + productData);
             * String finalResult = jerseyClientPost.doPostRequest(productSearchUrl, productData);
             */
            
            _LOGGER.info("Result : " + response);
            return response;
        } catch (Exception e) {
            System.out.println();
            _LOGGER.error("Exception while posting product to Radar API", e);
            return "{Message : Not valid}";
        }
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
