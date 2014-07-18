package com.asi.ext.api.radar.lookup.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class SetCodeValueJsonModel {

    @JsonProperty("ID")
    private String  id;
    @JsonProperty("CodeValue")
    private String  codeValue;
    @JsonProperty("DisplaySequence")
    private String  displaySequence;
    @JsonProperty("IsSupplierSpecific")
    private boolean isSupplierSpecific;

    public SetCodeValueJsonModel() {
    }
    /**
     * @param id
     * @param codeValue
     * @param displaySequence
     * @param isSupplierSpecific
     */
    public SetCodeValueJsonModel(String id, String codeValue, String displaySequence, boolean isSupplierSpecific) {
        this.id = id;
        this.codeValue = codeValue;
        this.displaySequence = displaySequence;
        this.isSupplierSpecific = isSupplierSpecific;
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
     * @return the displaySequence
     */
    public String getDisplaySequence() {
        return displaySequence;
    }

    /**
     * @param displaySequence
     *            the displaySequence to set
     */
    public void setDisplaySequence(String displaySequence) {
        this.displaySequence = displaySequence;
    }

    /**
     * @return the isSupplierSpecific
     */
    public boolean isSupplierSpecific() {
        return isSupplierSpecific;
    }

    /**
     * @param isSupplierSpecific
     *            the isSupplierSpecific to set
     */
    public void setSupplierSpecific(boolean isSupplierSpecific) {
        this.isSupplierSpecific = isSupplierSpecific;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codeValue == null) ? 0 : codeValue.hashCode());
        result = prime * result + ((displaySequence == null) ? 0 : displaySequence.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isSupplierSpecific ? 1231 : 1237);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SetCodeValueJsonModel other = (SetCodeValueJsonModel) obj;
        if (codeValue == null) {
            if (other.codeValue != null) return false;
        } else if (!codeValue.equals(other.codeValue)) return false;
        if (displaySequence == null) {
            if (other.displaySequence != null) return false;
        } else if (!displaySequence.equals(other.displaySequence)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (isSupplierSpecific != other.isSupplierSpecific) return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SetCodeValueJsonModel [id=" + id + ", codeValue=" + codeValue + ", displaySequence=" + displaySequence
                + ", isSupplierSpecific=" + isSupplierSpecific + "]";
    }

    
}
