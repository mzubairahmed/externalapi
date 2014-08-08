package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.service.model.Catalog;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductMediaCitations;

public class CatalogCriteriaParser {
	
    private final static Logger LOGGER           = Logger.getLogger(ProductNumberCriteriaParser.class.getName());
    public ProductDataStore     productDataStore = new ProductDataStore();
    
    @SuppressWarnings("static-access")
	public List<ProductMediaCitations> prepareProductCatalog(List<Catalog> catalogs, ProductDetail radProduct) {

    	LOGGER.info("Started processing Product Media Citations (Catalogs)");
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("User provided catalogs : " + catalogs);
        }
    	
        Integer newProductMediaCitationId = -1;
        List<ProductMediaCitations> productMediaCitations = new ArrayList<ProductMediaCitations>();
        ProductMediaCitations productMediaCitation;
        
        if(catalogs != null && !catalogs.isEmpty()) {
	    	for (Catalog catalog : catalogs) {
	    		productMediaCitation = productDataStore.getMediaCitationsByName(radProduct.getID(), catalog.getCatalogName(), catalog.getCatalogPage(), radProduct.getCompanyId());
	    		if(Integer.parseInt(productMediaCitation.getId()) < 0) {
	    			productMediaCitation.setId(String.valueOf(--newProductMediaCitationId));
	    		}
	    		productMediaCitations.add(productMediaCitation);
	    	}
        }
        LOGGER.info("Catalog processed...");
        return productMediaCitations;
    }

}
