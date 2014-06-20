package com.asi.service.product.client.vo;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CriteriaSetCodeValues {
    @JsonProperty("ID")
    private String                       id                         = "0";
    @JsonProperty("CriteriaSetValueId")
    private String                       criteriaSetValueId;
    private String                       setCodeValueId             = "";
    @JsonProperty("CodeValue")
    private String                       codeValue                  = "";
    @JsonProperty("CodeValueDetail")
    private String                       codeValueDetail            = "";
    @JsonProperty("ChildCriteriaSetCodeValues")
    private ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues = {};

    public CriteriaSetCodeValues() {
        // Default constructor
    }
    /**
     * @param id
     * @param criteriaSetValueId
     * @param setCodeValueId
     * @param codeValue
     * @param codeValueDetail
     * @param childCriteriaSetCodeValues
     */
    public CriteriaSetCodeValues(String id, String criteriaSetValueId, String setCodeValueId, String codeValue,
            String codeValueDetail, ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues) {
        this.id = id;
        this.criteriaSetValueId = criteriaSetValueId;
        this.setCodeValueId = setCodeValueId;
        this.codeValue = codeValue;
        this.codeValueDetail = codeValueDetail;
        this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
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
     * @param criteriaSetValueId
     *            the criteriaSetValueId to set
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
     * @param setCodeValueId
     *            the setCodeValueId to set
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
     * @param codeValue
     *            the codeValue to set
     */
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    /**
     * @return the codeValueDetail
     */
    public String getCodeValueDetail() {
        return codeValueDetail;
    }

    /**
     * @param codeValueDetail
     *            the codeValueDetail to set
     */
    public void setCodeValueDetail(String codeValueDetail) {
        this.codeValueDetail = codeValueDetail;
    }

    /**
     * @return the childCriteriaSetCodeValues
     */
    public ChildCriteriaSetCodeValues[] getChildCriteriaSetCodeValues() {
        return childCriteriaSetCodeValues;
    }

    /**
     * @param childCriteriaSetCodeValues
     *            the childCriteriaSetCodeValues to set
     */
    public void setChildCriteriaSetCodeValues(ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues) {
        this.childCriteriaSetCodeValues = childCriteriaSetCodeValues;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CriteriaSetCodeValues [id=" + id + ", criteriaSetValueId=" + criteriaSetValueId + ", setCodeValueId="
                + setCodeValueId + ", codeValue=" + codeValue + ", codeValueDetail=" + codeValueDetail
                + ", childCriteriaSetCodeValues=" + Arrays.toString(childCriteriaSetCodeValues) + "]";
    }
    
    
    

}

