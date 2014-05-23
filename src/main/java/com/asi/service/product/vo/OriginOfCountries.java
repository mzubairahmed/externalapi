package com.asi.service.product.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "origins")
public class OriginOfCountries {
	List<CountryOfOrigin> originOfCountryList;

	public List<CountryOfOrigin> getOriginOfCountryList() {
		return originOfCountryList;
	}

	public void setOriginOfCountryList(List<CountryOfOrigin> originOfCountryList) {
		this.originOfCountryList = originOfCountryList;
	}
	
}
