package com.asi.ext.api.radar.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChildCriteriaSetCodeValue {

    @JsonProperty("ID")
    private String                          id                           = "";
    @JsonProperty("CriteriaSetValueId")
    private String criteriaSetValueId;
    @JsonProperty("SetCodeValueId")
    private String setCodeValueId;
    @JsonProperty("CodeValue")
    private String codeValue;
    @JsonProperty("ChildCriteriaSetCodeValues")
    private List<ChildCriteriaSetCodeValues> childCriteriaSetCodeValues = new ArrayList<ChildCriteriaSetCodeValues>();
    
    public ChildCriteriaSetCodeValue() {
        // Default constructor
    }
    /**
     * @param id
     * @param criteriaSetValueId
     * @param setCodeValueId
     * @param codeValue
     * @param childCriteriaSetCodeValues
     */
    public ChildCriteriaSetCodeValue(String id, String criteriaSetValueId, String setCodeValueId, String codeValue,
            List<ChildCriteriaSetCodeValues> childCriteriaSetCodeValues) {
        this.id = id;
        this.criteriaSetValueId = criteriaSetValueId;
        this.setCodeValueId = setCodeValueId;
        this.codeValue = codeValue;
        this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the criteriaSetValueId
     */
    public String getCriteriaSetValueId() {
        return criteriaSetValueId;
    }
    /**
     * @param criteriaSetValueId the criteriaSetValueId to set
     */
    public void setCriteriaSetValueId(String criteriaSetValueId) {
        this.criteriaSetValueId = criteriaSetValueId;
    }
    /**
     * @return the setCodeValueId
     */
    public String getSetCodeValueId() {
        return setCodeValueId;
    }
    /**
     * @param setCodeValueId the setCodeValueId to set
     */
    public void setSetCodeValueId(String setCodeValueId) {
        this.setCodeValueId = setCodeValueId;
    }
    /**
     * @return the codeValue
     */
    public String getCodeValue() {
        return codeValue;
    }
    /**
     * @param codeValue the codeValue to set
     */
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
    /**
     * @return the childCriteriaSetCodeValues
     */
    public List<ChildCriteriaSetCodeValues> getChildCriteriaSetCodeValues() {
        return childCriteriaSetCodeValues;
    }
    /**
     * @param childCriteriaSetCodeValues the childCriteriaSetCodeValues to set
     */
    public void setChildCriteriaSetCodeValues(List<ChildCriteriaSetCodeValues> childCriteriaSetCodeValues) {
        this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ChildCriteriaSetCodeValue [id=" + id + ", criteriaSetValueId=" + criteriaSetValueId + ", setCodeValueId="
                + setCodeValueId + ", codeValue=" + codeValue + ", childCriteriaSetCodeValues=" + childCriteriaSetCodeValues + "]";
    }
    
    
}
