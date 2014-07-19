package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Color {

    @JsonProperty("Name")
    private String      name;
    @JsonProperty("Alias")
    private String      alias;
    @JsonProperty("Combos")
    private List<Combo> combos = new ArrayList<Combo>();

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
    public List<Combo> getCombos() {
        return combos;
    }

    /**
     * @param combos
     *            the combos to set
     */
    public void setCombos(List<Combo> combos) {
        this.combos = combos;
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
