package com.asi.ext.api.radar.lookup.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Rahul K
 *
 */
public class CodeValueGroupJsonModel {
    
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("SetCodeValues")
    private List<SetCodeValueJsonModel> setCodeValues = new ArrayList<>();
    
    // Default constructor
    public CodeValueGroupJsonModel() {
    }

    /**
     * Full Constructor
     * @param code
     * @param description
     * @param displayName
     * @param setCodeValue
     */
    public CodeValueGroupJsonModel(String code, String description, String displayName, List<SetCodeValueJsonModel> setCodeValues) {
        this.code = code;
        this.description = description;
        this.displayName = displayName;
        this.setCodeValues = setCodeValues;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the setCodeValues
     */
    public List<SetCodeValueJsonModel> getSetCodeValues() {
        return setCodeValues;
    }

    /**
     * @param setCodeValues the setCodeValues to set
     */
    public void setSetCodeValues(List<SetCodeValueJsonModel> setCodeValues) {
        this.setCodeValues = setCodeValues;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((setCodeValues == null) ? 0 : setCodeValues.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CodeValueGroupJsonModel other = (CodeValueGroupJsonModel) obj;
        if (code == null) {
            if (other.code != null) return false;
        } else if (!code.equals(other.code)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (displayName == null) {
            if (other.displayName != null) return false;
        } else if (!displayName.equals(other.displayName)) return false;
        if (setCodeValues == null) {
            if (other.setCodeValues != null) return false;
        } else if (!setCodeValues.equals(other.setCodeValues)) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CodeValueGroup [code=" + code + ", description=" + description + ", displayName=" + displayName
                + ", setCodeValues=" + setCodeValues + "]";
    }

}
