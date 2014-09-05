package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Packages")
public class PackagesList {


		private List<String> packages = new ArrayList<>();

		public List<String> getPackages() {
			return packages;
		}

		public void setPackages(List<String> packages) {
			this.packages = packages;
		}
			
		
	}
