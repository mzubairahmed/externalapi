/**
 * 
 */
package com.asi.ext.api.service;

import org.springframework.http.HttpHeaders;

import com.asi.ext.api.service.model.Product;
import com.asi.service.resource.response.ExternalAPIResponse;




/**
 * @author krahul
 *
 */
public interface ProductService {
    
    public Product getProduct(String companyId, String xid);
    
    public ExternalAPIResponse updateProduct(String authToken, String companyId, String xid, Product serviceProduct);

}
