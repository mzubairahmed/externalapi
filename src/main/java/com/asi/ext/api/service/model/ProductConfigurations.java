package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductConfigurations {

    @JsonProperty("ImprintColors")
    private ImprintColor              imprintColors        = null;
    @JsonProperty("Samples")
    private Samples                   samples;
    @JsonProperty("Colors")
    private List<Color>               colors               = new ArrayList<Color>();
    @JsonProperty("Materials")
    private List<Material>            materials            = new ArrayList<Material>();
    @JsonProperty("Sizes")
    private Size                sizes                = new Size();
    @JsonProperty("Shapes")
    private List<String>              shapes               = new ArrayList<String>();
    @JsonProperty("Options")
    private List<Option>              options              = new ArrayList<Option>();
    @JsonProperty("Origins")
    private List<String>              origins              = new ArrayList<String>();
    @JsonProperty("Packaging")
    private List<String>              packaging            = new ArrayList<String>();
    @JsonProperty("TradeNames")
    private List<String>              tradeNames           = new ArrayList<String>();
    @JsonProperty("ImprintMethods")
    private List<ImprintMethod>       imprintMethods       = new ArrayList<ImprintMethod>();
    @JsonProperty("ProductionTime")
    private List<ProductionTime>      productionTime       = new ArrayList<ProductionTime>();
    @JsonProperty("RushTime")
    private List<RushTime>            rushTime             = new ArrayList<RushTime>();
    @JsonProperty("AdditionalColors")
    private List<String>              additionalColors     = new ArrayList<String>();
    @JsonProperty("AdditionalLocations")
    private List<String>              additionalLocations  = new ArrayList<String>();
    @JsonProperty("ImprintSizeLocations")
    private List<ImprintSizeLocation> imprintSizeLocations = new ArrayList<ImprintSizeLocation>();
    @JsonProperty("ShippingEstimates")
    private List<ShippingEstimate>    shippingEstimates    = new ArrayList<ShippingEstimate>();

    @JsonProperty("Colors")
    public List<Color> getColors() {
        return colors;
    }

    @JsonProperty("Colors")
    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    @JsonProperty("Materials")
    public List<Material> getMaterials() {
        return materials;
    }

    @JsonProperty("Materials")
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    @JsonProperty("Sizes")
    public Size getSizes() {
        return sizes;
    }

    @JsonProperty("Sizes")
    public void setSizes(Size sizes) {
        this.sizes = sizes;
    }

    @JsonProperty("Shapes")
    public List<String> getShapes() {
        return shapes;
    }

    @JsonProperty("Shapes")
    public void setShapes(List<String> shapes) {
        this.shapes = shapes;
    }

    @JsonProperty("Options")
    public List<Option> getOptions() {
        return options;
    }

    @JsonProperty("Options")
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @JsonProperty("Origins")
    public List<String> getOrigins() {
        return origins;
    }

    @JsonProperty("Origins")
    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }

    @JsonProperty("Packaging")
    public List<String> getPackaging() {
        return packaging;
    }

    @JsonProperty("Packaging")
    public void setPackaging(List<String> packaging) {
        this.packaging = packaging;
    }

    @JsonProperty("TradeNames")
    public List<String> getTradeNames() {
        return tradeNames;
    }

    @JsonProperty("TradeNames")
    public void setTradeNames(List<String> tradeNames) {
        this.tradeNames = tradeNames;
    }

    @JsonProperty("ImprintMethods")
    public List<ImprintMethod> getImprintMethods() {
        return imprintMethods;
    }

    @JsonProperty("ImprintMethods")
    public void setImprintMethods(List<ImprintMethod> imprintMethods) {
        this.imprintMethods = imprintMethods;
    }

    @JsonProperty("ProductionTime")
    public List<ProductionTime> getProductionTime() {
        return productionTime;
    }

    @JsonProperty("ProductionTime")
    public void setProductionTime(List<ProductionTime> productionTime) {
        this.productionTime = productionTime;
    }

    @JsonProperty("RushTime")
    public List<RushTime> getRushTime() {
        return rushTime;
    }

    @JsonProperty("RushTime")
    public void setRushTime(List<RushTime> rushTime) {
        this.rushTime = rushTime;
    }

    @JsonProperty("AdditionalColors")
    public List<String> getAdditionalColors() {
        return additionalColors;
    }

    @JsonProperty("AdditionalColors")
    public void setAdditionalColors(List<String> additionalColors) {
        this.additionalColors = additionalColors;
    }

    @JsonProperty("AdditionalLocations")
    public List<String> getAdditionalLocations() {
        return additionalLocations;
    }

    @JsonProperty("AdditionalLocations")
    public void setAdditionalLocations(List<String> additionalLocations) {
        this.additionalLocations = additionalLocations;
    }

    @JsonProperty("ImprintColors")
    public ImprintColor getImprintColors() {
        return imprintColors;
    }

    @JsonProperty("ImprintColors")
    public void setImprintColors(ImprintColor imprintColors) {
        this.imprintColors = imprintColors;
    }

    @JsonProperty("ImprintSizeLocations")
    public List<ImprintSizeLocation> getImprintSizeLocations() {
        return imprintSizeLocations;
    }

    @JsonProperty("ImprintSizeLocations")
    public void setImprintSizeLocations(List<ImprintSizeLocation> imprintSizeLocations) {
        this.imprintSizeLocations = imprintSizeLocations;
    }

    /**
     * @return the samples
     */
    @JsonProperty("Samples")
    public Samples getSamples() {
        return samples;
    }

    /**
     * @param samples
     *            the samples to set
     */
    @JsonProperty("Samples")
    public void setSamples(Samples samples) {
        this.samples = samples;
    }

    @JsonProperty("ShippingEstimates")
    public List<ShippingEstimate> getShippingEstimates() {
        return shippingEstimates;
    }

    @JsonProperty("ShippingEstimates")
    public void setShippingEstimates(List<ShippingEstimate> shippingEstimates) {
        this.shippingEstimates = shippingEstimates;
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
