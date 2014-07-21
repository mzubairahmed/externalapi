/**
 * 
 */
package com.asi.ext.api.service;

import com.asi.ext.api.service.model.Product;




/**
 * @author krahul
 *
 */
public interface ProductService {
    
    public Product getProduct(String companyId, String xid);
    
    public String updateProduct(String companyId, String xid, Product serviceProduct);

}
