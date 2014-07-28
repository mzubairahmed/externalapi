package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.Relationship;

public class ImprintRelationData {

    private List<Relationship>               relationships          = new ArrayList<Relationship>();
    private Map<String, ProductCriteriaSets> existingCriteriaSetMap = new HashMap<String, ProductCriteriaSets>();

    /**
     * @return the relationships
     */
    public List<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * @param relationships
     *            the relationships to set
     */
    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    /**
     * @return the existingCriteriaSetMap
     */
    public Map<String, ProductCriteriaSets> getExistingCriteriaSetMap() {
        return existingCriteriaSetMap;
    }

    /**
     * @param existingCriteriaSetMap
     *            the existingCriteriaSetMap to set
     */
    public void setExistingCriteriaSetMap(Map<String, ProductCriteriaSets> existingCriteriaSetMap) {
        this.existingCriteriaSetMap = existingCriteriaSetMap;
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
