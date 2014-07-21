package com.asi.service.product.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.radar.model.Product;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.exception.ProductNotFoundException;

@Component
public class ProductClient {
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate          restTemplate;
    private String        productSearchUrl;
    private static Logger _LOGGER = LoggerFactory.getLogger(ProductClient.class);

    public ProductDetail doIt(String companyID, String productID) throws ProductNotFoundException {
        return searchProduct(companyID, productID);
    }

    public Product getRadarProduct(String companyID, String productID) throws ProductNotFoundException {
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
            throw exc;
        }
        return product;
    }

    private Product searchRadarProduct(String companyID, String productID) throws ProductNotFoundException

    {
        companyID = "243";
        productID = "indxid";
        String productSearchUrl = getProductSearchUrl() + "?companyId={companyID}&externalProductId={productID}";

        Product product = null;
        try {
            //ProductDetail details = restTemplate.getForObject(productSearchUrl, ProductDetail.class, companyID, productID);
            product = restTemplate.getForObject(productSearchUrl, Product.class, companyID, productID);
            //System.out.println(details);

        } catch (RestClientException ex) {
            _LOGGER.error(ex.getMessage());
            throw new ProductNotFoundException(productID);
        }
        return product;
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
