package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Material {

    @JsonProperty("Name")
    private String              name;
    @JsonProperty("Alias")
    private String              alias;
    @JsonProperty("Combo")
    private Combo         combos         = null;
    @JsonProperty("BlendMaterials")
    private List<BlendMaterial> blendMaterials = new ArrayList<BlendMaterial>();

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("Alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the combos
     */
    @JsonProperty("Combo")
    public Combo getCombo() {
        return combos;
    }

    /**
     * @param combos
     *            the combos to set
     */
    @JsonProperty("Combo")
    public void setCombo(Combo combos) {
        this.combos = combos;
    }

    /**
     * @return the blendMaterials
     */
    public List<BlendMaterial> getBlendMaterials() {
        return blendMaterials;
    }

    /**
     * @param blendMaterials
     *            the blendMaterials to set
     */
    public void setBlendMaterials(List<BlendMaterial> blendMaterials) {
        this.blendMaterials = blendMaterials;
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

}
