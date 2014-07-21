package com.asi.service.product.client.vo;

import java.util.ArrayList;
import java.util.List;

public class UpChargePriceDetails {

    private String       upChargeName      = "";
    private String       upChargeCriteria1 = "";
    private String       upChargeCriteria2 = "";
    private String       upchargeType      = "";
    private String       upchargeLevel     = "";
    private String       uQUR              = "";
    private List<String> prices            = new ArrayList<String>();
    private List<String> quantities        = new ArrayList<String>();
    private List<String> discounts         = new ArrayList<String>();

    /**
     * @return the upChargeName
     */
    public String getUpChargeName() {
        return upChargeName;
    }

    /**
     * @param upChargeName
     *            the upChargeName to set
     */
    public void setUpChargeName(String upChargeName) {
        this.upChargeName = upChargeName;
    }

    /**
     * @return the upChargeCriteria1
     */
    public String getUpChargeCriteria1() {
        return upChargeCriteria1;
    }

    /**
     * @param upChargeCriteria1
     *            the upChargeCriteria1 to set
     */
    public void setUpChargeCriteria1(String upChargeCriteria1) {
        this.upChargeCriteria1 = upChargeCriteria1;
    }

    /**
     * @return the upChargeCriteria2
     */
    public String getUpChargeCriteria2() {
        return upChargeCriteria2;
    }

    /**
     * @param upChargeCriteria2
     *            the upChargeCriteria2 to set
     */
    public void setUpChargeCriteria2(String upChargeCriteria2) {
        this.upChargeCriteria2 = upChargeCriteria2;
    }

    /**
     * @return the upchargeType
     */
    public String getUpchargeType() {
        return upchargeType;
    }

    /**
     * @param upchargeType
     *            the upchargeType to set
     */
    public void setUpchargeType(String upchargeType) {
        this.upchargeType = upchargeType;
    }

    /**
     * @return the upchargeLevel
     */
    public String getUpchargeLevel() {
        return upchargeLevel;
    }

    /**
     * @param upchargeLevel
     *            the upchargeLevel to set
     */
    public void setUpchargeLevel(String upchargeLevel) {
        this.upchargeLevel = upchargeLevel;
    }

    /**
     * @return the uQUR
     */
    public String getuQUR() {
        return uQUR;
    }

    /**
     * @param uQUR
     *            the uQUR to set
     */
    public void setuQUR(String uQUR) {
        this.uQUR = uQUR;
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

}
