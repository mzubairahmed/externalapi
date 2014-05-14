package com.asi.service.product.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
@XmlRootElement

public class ProductDataSheet  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    
    private Integer iD;

    private Integer productId;

    private Integer companyId;

    private String url;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getID() {
        return iD;
    }

 
    public void setID(Integer iD) {
        this.iD = iD;
    }

  
    public Integer getProductId() {
        return productId;
    }


    public void setProductId(Integer productId) {
        this.productId = productId;
    }

 
    public Integer getCompanyId() {
        return companyId;
    }

 
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

 
    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}