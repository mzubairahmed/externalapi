package com.asi.service.product.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "itemPrice")
public class Wrapper {
	 
    private List<ItemPriceDetail> items;
 
    public Wrapper() {
        items = new ArrayList<ItemPriceDetail>();
    }

    @XmlElement(name = "itemPrices", required = true)
    public List<ItemPriceDetail> getItems() {
        return items;
    }

	public void setItems(List<ItemPriceDetail> items) {
		this.items = items;
	}
}