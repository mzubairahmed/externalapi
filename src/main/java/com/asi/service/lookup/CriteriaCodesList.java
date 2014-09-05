package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "criteriacodes")
public class CriteriaCodesList {
		private List<String> criteriacodes = new ArrayList<>();

		public List<String> getCriteriaCodes() {
			return criteriacodes;
		}

		public void setCriteriaCodes(List<String> criteriacodes) {
			this.criteriacodes = criteriacodes;
		}

}