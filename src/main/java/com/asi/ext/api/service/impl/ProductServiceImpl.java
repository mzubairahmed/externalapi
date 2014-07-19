/**
 * 
 */
package com.asi.ext.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.core.repo.product.ProductRepo;
import com.asi.ext.api.service.ProductService;
import com.asi.ext.api.service.model.Product;

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
    public Product updateProduct(String companyId, String xid, Product serviceProduct) {
        // TODO Auto-generated method stub
        return productRepo.updateProduct(companyId, xid, serviceProduct);
    }

    /**
     * @param productRepo
     *            the productRepo to set
     */
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

}
