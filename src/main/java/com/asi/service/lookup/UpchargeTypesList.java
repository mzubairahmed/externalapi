package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "upchargetypes")
public class UpchargeTypesList {
		private List<String> upchargeTypes = new ArrayList<>();

		public List<String> getUpchargeTypes() {
			return upchargeTypes;
		}

		public void setUpchargeTypes(List<String> upchargeTypes) {
			this.upchargeTypes = upchargeTypes;
		}

}