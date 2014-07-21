package com.asi.service.product.vo;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "basePrices")
public class Prices {
	private List<PriceDetail> priceDetailsList;
    public List<PriceDetail> getImprintMethod() {
		return priceDetailsList;
	}

	public void setImprintMethod(List<PriceDetail> priceDetailsList) {
		this.priceDetailsList = priceDetailsList;
	}

}
