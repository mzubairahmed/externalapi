/**
 * 
 */
package com.asi.ext.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.asi.core.repo.product.ProductRepo;
import com.asi.ext.api.service.ProductService;
import com.asi.ext.api.service.model.Product;
import com.asi.service.resource.response.ExternalAPIResponse;

/**
 * @author krahul
 * 
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.service.ProductService#getProduct(java.lang.String, java.lang.String)
     */
    @Override
    public Product getProduct(String companyId, String xid) {
        // TODO Auto-generated method stub
        return productRepo.getProduct(companyId, xid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.service.ProductService#updateProduct(java.lang.String, java.lang.String,
     * com.asi.ext.api.service.model.Product)
     */
    @Override
    public ExternalAPIResponse updateProduct(String authToken, String companyId, String xid, Product serviceProduct) {
        // TODO Auto-generated method stub
        return productRepo.updateProduct(authToken, companyId, xid, serviceProduct);
    }

    /**
     * @param productRepo
     *            the productRepo to set
     */
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

}
