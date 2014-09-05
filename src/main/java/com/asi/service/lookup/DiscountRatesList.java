package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "discountrates")
public class DiscountRatesList {
		private List<String> discountRates = new ArrayList<>();

		public List<String> getDiscountRates() {
			return discountRates;
		}

		public void setDiscountRates(List<String> discountRates) {
			this.discountRates = discountRates;
		}

}