package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductKeywords;

public class ProductKeywordProcessor {

    public List<ProductKeywords> getProductKeywords(List<String> keywordList, ProductDetail extProduct, boolean updateNeeded) {
        String[] productKeywords = (keywordList != null && !keywordList.isEmpty()) ? CommonUtilities.convertStringListToCSV(keywordList).split(",") : null;
        
        if (productKeywords == null) {
            return new ArrayList<ProductKeywords>();
        }
        HashMap<String, ProductKeywords> prdKeywordMap = new HashMap<String, ProductKeywords>();
        List<ProductKeywords> finalKeywordList = new ArrayList<ProductKeywords>();

        if (extProduct != null && extProduct.getProductKeywords() != null && extProduct.getProductKeywords().size() > 0) {
            if (updateNeeded) {
                prdKeywordMap = getExistingKeyWordMap(extProduct.getProductKeywords());
            } else {
                return extProduct.getProductKeywords();
            }
        }
        // if the existing product dosen't have any keywords and the updateNeeded == true
        // then return empty
        if (prdKeywordMap.isEmpty() && !updateNeeded) {
            return new ArrayList<ProductKeywords>();
        }
        // Compare and update each keywords
        if (productKeywords != null && productKeywords.length > 0) {
            for (String keyword : productKeywords) {
                if (!CommonUtilities.isValueNull(keyword)) {
                    ProductKeywords tempKeyword = prdKeywordMap.get(keyword.trim().toUpperCase());
                    if (tempKeyword != null) {
                        finalKeywordList.add(tempKeyword);
                    } else {
                        finalKeywordList.add(createProductKeyword(keyword, extProduct));
                    }
                }

            }
        }
        return finalKeywordList;
    }

    private ProductKeywords createProductKeyword(String value, ProductDetail product) {
        ProductKeywords prdctKeyWrd = new ProductKeywords();

        prdctKeyWrd.setId(ApplicationConstants.CONST_STRING_ZERO);
        prdctKeyWrd.setMarketSegmentCode(ApplicationConstants.CONST_MARKET_SEGMENT_CODE);
        prdctKeyWrd.setProductId(product != null ? product.getID() : "0");
        prdctKeyWrd.setValue(value.trim());
        prdctKeyWrd.setTypeCode(ApplicationConstants.PRODUCT_KEYWORD_TYPE_CODE);

        return prdctKeyWrd;
    }

    private HashMap<String, ProductKeywords> getExistingKeyWordMap(List<ProductKeywords> productKeywords) {
        HashMap<String, ProductKeywords> keywordMap = new HashMap<>();
        for (ProductKeywords keyword : productKeywords) {
            if (keyword != null) {
                keywordMap.put(keyword.getValue().toUpperCase(), keyword);
            }
        }
        return keywordMap;
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
