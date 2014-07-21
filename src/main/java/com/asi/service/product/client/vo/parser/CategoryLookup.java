package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


import com.asi.core.utils.JerseyClient;
import com.asi.ext.api.integration.lookup.parser.CategoryParser;
import com.asi.service.product.client.vo.SelectedProductCategory;



public class CategoryLookup {
    
    private final static Logger _LOGGER = Logger.getLogger(CategoryLookup.class.getName());

    public String                                    categoryLookupApi;
    public CategoryParser                            categoryParser;

    private static ConcurrentHashMap<String, String> categoriesMap = new ConcurrentHashMap<String, String>();

    public CategoryLookup() {
    }

    private synchronized void loadCategoryCollection() {
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("CategoryLookup.loadCategoryCollection started");
        }
        if (categoriesMap != null && !categoriesMap.isEmpty()) {
            return; // Categories already loaded
        } else {
            load();
        }
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("CategoryLookup.loadCategoryCollection completed");
        }
    }

    private void load() {
        String response = null;
        try {
            response = JerseyClient.invoke(new URI(categoryLookupApi));            
            categoriesMap = categoryParser.getCategoryCollectionFromJSON(response);
            
            if (categoriesMap != null && !categoriesMap.isEmpty()) {
                _LOGGER.info("Building of category collection completed successfuly");
            } else {
                _LOGGER.info("Failed to build category collection");
            }
        } catch (Exception e) {
            _LOGGER.error("Exception while trying load category collection", e);
        }
    }
    
    private String getCategory(String categoryCode) {
        if (categoriesMap != null && !categoriesMap.isEmpty()) {
            return categoriesMap.get(categoryCode);
        } else {
            loadCategoryCollection();
            return categoriesMap != null ? categoriesMap.get(categoryCode) : null;
        }
    }

    public String getCategoryString(List<SelectedProductCategory> productCategories) {
        String categories = "";
        if (productCategories != null && !productCategories.isEmpty()) {
            for (SelectedProductCategory category : productCategories) {
                if (category != null && !category.getAdCategoryFlg()) {
                    String temp = getCategory(category.getCode());
                    
                    if (temp != null) {
                        categories += "," + temp;
                    } else {
                        //TODO : If a category not found then we need to add to batch error
                    }
                }
            }
        } else {
        }
        if (categories != null && categories.startsWith(",")) {
            categories = categories.substring(1);
        }
        return categories;
    }
    /**
     * @return the categoryLookupApi
     */
    @Required
    public String getCategoryLookupApi() {
        return categoryLookupApi;
    }

    /**
     * @param categoryLookupApi
     *            the categoryLookupApi to set
     */
    @Required
    public void setCategoryLookupApi(String categoryLookupApi) {
        this.categoryLookupApi = categoryLookupApi;
    }

    /**
     * @return the categoryParser
     */
    @Required
    public CategoryParser getCategoryParser() {
        return categoryParser;
    }

    /**
     * @param categoryParser the categoryParser to set
     */
    @Required
    public void setCategoryParser(CategoryParser categoryParser) {
        this.categoryParser = categoryParser;
    }    

}
