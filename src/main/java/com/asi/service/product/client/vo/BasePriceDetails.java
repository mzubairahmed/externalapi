package com.asi.service.product.client.vo;

import java.util.ArrayList;
import java.util.List;

public class BasePriceDetails {
    private String       basePriceName      = "";
    private String       basePriceCriteria1 = "";
    private String       basePriceCriteria2 = "";
    private String       priceIncludes      = "";
    private String       QUR                = "";
    private String       currency           = "";
    private String       productNumberPrice = "";
    private List<String> prices             = new ArrayList<String>();
    private List<String> quantities         = new ArrayList<String>();
    private List<String> discounts          = new ArrayList<String>();

    private boolean      isMultiPriceGrid   = false;

    /**
     * @return the basePriceName
     */
    public String getBasePriceName() {
        return basePriceName;
    }

    /**
     * @param basePriceName
     *            the basePriceName to set
     */
    public void setBasePriceName(String basePriceName) {
        this.basePriceName = basePriceName;
    }

    /**
     * @return the basePriceCriteria1
     */
    public String getBasePriceCriteria1() {
        return basePriceCriteria1;
    }

    /**
     * @param basePriceCriteria1
     *            the basePriceCriteria1 to set
     */
    public void setBasePriceCriteria1(String basePriceCriteria1) {
        this.basePriceCriteria1 = basePriceCriteria1;
    }

    /**
     * @return the basePriceCriteria2
     */
    public String getBasePriceCriteria2() {
        return basePriceCriteria2;
    }

    /**
     * @param basePriceCriteria2
     *            the basePriceCriteria2 to set
     */
    public void setBasePriceCriteria2(String basePriceCriteria2) {
        this.basePriceCriteria2 = basePriceCriteria2;
    }

    /**
     * @return the priceIncludes
     */
    public String getPriceIncludes() {
        return priceIncludes;
    }

    /**
     * @param priceIncludes
     *            the priceIncludes to set
     */
    public void setPriceIncludes(String priceIncludes) {
        this.priceIncludes = priceIncludes;
    }

    /**
     * @return the qUR
     */
    public String getQUR() {
        return QUR;
    }

    /**
     * @param qUR
     *            the qUR to set
     */
    public void setQUR(String qUR) {
        QUR = qUR;
    }

    /**
     * @return the productNumberPrice
     */
    public String getProductNumberPrice() {
        return productNumberPrice;
    }

    /**
     * @param productNumberPrice
     *            the productNumberPrice to set
     */
    public void setProductNumberPrice(String productNumberPrice) {
        this.productNumberPrice = productNumberPrice;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the prices
     */
    public List<String> getPrices() {
        return prices;
    }

    /**
     * @param prices
     *            the prices to set
     */
    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    /**
     * @return the quantities
     */
    public List<String> getQuantities() {
        return quantities;
    }

    /**
     * @param quantities
     *            the quantities to set
     */
    public void setQuantities(List<String> quantities) {
        this.quantities = quantities;
    }

    /**
     * @return the discounts
     */
    public List<String> getDiscounts() {
        return discounts;
    }

    /**
     * @param discounts
     *            the discounts to set
     */
    public void setDiscounts(List<String> discounts) {
        this.discounts = discounts;
    }

    /**
     * @return the isMultiPriceGrid
     */
    public boolean isMultiPriceGrid() {
        return isMultiPriceGrid;
    }

    /**
     * @param isMultiPriceGrid
     *            the isMultiPriceGrid to set
     */
    public void setMultiPriceGrid(boolean isMultiPriceGrid) {
        this.isMultiPriceGrid = isMultiPriceGrid;
    }

}
