package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "upchargelevels")
public class UpchargeLevelsList {
		private List<String> upchargeLevels = new ArrayList<>();

		public List<String> getUpchargeLevels() {
			return upchargeLevels;
		}

		public void setUpchargeLevels(List<String> upchargeLevels) {
			this.upchargeLevels = upchargeLevels;
		}

}