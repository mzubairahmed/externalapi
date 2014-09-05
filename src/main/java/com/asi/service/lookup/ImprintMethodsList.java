package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "imprintMethods")
public class ImprintMethodsList {
		private List<String> imprintMethodsList = new ArrayList<>();

		public List<String> getImprintMethods() {
			return imprintMethodsList;
		}

		public void setImprintMethods(List<String> imprintMethodsList) {
			this.imprintMethodsList = imprintMethodsList;
		}

}
