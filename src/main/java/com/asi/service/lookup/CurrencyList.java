package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "currencies")
public class CurrencyList {
		private List<String> currencies = new ArrayList<>();

		public List<String> getCurrencies() {
			return currencies;
		}

		public void setCurrencies(List<String> currencies) {
			this.currencies = currencies;
		}

}
