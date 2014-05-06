package com.asi.service.product.client.vo.parser;

import java.util.List;

import com.asi.service.product.client.vo.ProductCriteriaSet;

public class ImprintParser {
	 /**
     * Find a criteriaSet from the productCriteria set array based on the criteria code
     * 
     * @param productCriteriaSetsAry
     *            is the array contains all criteria set of the product
     * @param criteriaCode
     *            is the criteria code of the criteriaSet to find
     * @return the matched {@linkplain ProductCriteriaSets } or null
     */
    public ProductCriteriaSet getCriteriaSetBasedOnCriteriaCode(List<ProductCriteriaSet> productCriteriaSetsAry, String criteriaCode) {
        for (ProductCriteriaSet currentProductCriteriaSet: productCriteriaSetsAry)
        	{
        		if (null != currentProductCriteriaSet && currentProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode.trim()))
        			return currentProductCriteriaSet;
        	}
        return null;
    }
   
}
