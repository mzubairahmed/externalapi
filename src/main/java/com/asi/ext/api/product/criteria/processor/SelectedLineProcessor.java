/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

/**
 * @author krahul
 *
 */
public class SelectedLineProcessor extends SimpleCriteriaProcessor {

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#getCriteriaSet(java.lang.String, com.asi.service.product.client.vo.ProductDetail, com.asi.service.product.client.vo.ProductCriteriaSets, int)
     */
    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#getSetCodeValueId(java.lang.String)
     */
    @Override
    public String getSetCodeValueId(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#isValueIsValid(java.lang.String)
     */
    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#processValues(java.lang.String)
     */
    @Override
    protected String[] processValues(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#updateCriteriaSet(java.lang.String)
     */
    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

}
