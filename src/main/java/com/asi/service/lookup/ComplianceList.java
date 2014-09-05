package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "Compliances")
public class ComplianceList {
		private List<String> compliances = new ArrayList<>();

		public List<String> getCompliances() {
			return compliances;
		}

		public void setCompliances(List<String> compliances) {
			this.compliances = compliances;
		}

}
