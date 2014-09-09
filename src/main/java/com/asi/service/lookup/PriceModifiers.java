package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "pricemodifiers")
public class PriceModifiers {
		private List<String> pricemodifiers = new ArrayList<>();

		public List<String> getPricemodifiers() {
			return pricemodifiers;
		}

		public void setPricemodifiers(List<String> pricemodifiers) {
			this.pricemodifiers = pricemodifiers;
		}

}
