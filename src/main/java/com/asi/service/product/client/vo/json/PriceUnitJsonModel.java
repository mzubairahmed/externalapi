package com.asi.service.product.client.vo.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.asi.service.product.client.vo.PriceUnit;


public class PriceUnitJsonModel extends PriceUnit implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4805697128036154987L;
    
    @JsonProperty("ID")
    private String ID;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("ItemsPerUnit")
    private String itemsPerUnit;
    
    public PriceUnitJsonModel() {
    }

    /**
     * @param iD
     * @param description
     * @param displayName
     * @param itemsPerUnit
     */
    public PriceUnitJsonModel(String iD, String description, String displayName, String itemsPerUnit) {
        ID = iD;
        this.description = description;
        this.displayName = displayName;
        this.itemsPerUnit = itemsPerUnit;
    }

    /**
     * @return the iD
     */
    public String getID() {
        return ID;
    }

    /**
     * @param iD the iD to set
     */
    public void setID(String iD) {
        ID = iD;
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
     * @return the itemsPerUnit
     */
    public String getItemsPerUnit() {
        return itemsPerUnit;
    }

    /**
     * @param itemsPerUnit the itemsPerUnit to set
     */
    public void setItemsPerUnit(String itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((itemsPerUnit == null) ? 0 : itemsPerUnit.hashCode());
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
        PriceUnitJsonModel other = (PriceUnitJsonModel) obj;
        if (ID == null) {
            if (other.ID != null) return false;
        } else if (!ID.equals(other.ID)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (displayName == null) {
            if (other.displayName != null) return false;
        } else if (!displayName.equals(other.displayName)) return false;
        if (itemsPerUnit == null) {
            if (other.itemsPerUnit != null) return false;
        } else if (!itemsPerUnit.equals(other.itemsPerUnit)) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PriceUnitJsonModel [ID=" + ID + ", description=" + description + ", displayName=" + displayName + ", itemsPerUnit="
                + itemsPerUnit + "]";
    }
    
    
}
