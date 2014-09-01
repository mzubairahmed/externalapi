package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.lookup.vo.LookupName;

@XmlRootElement(name = "Packages")
public class PackagesList {


		private List<LookupName> packages = new ArrayList<>();

		public List<LookupName> getPackages() {
			return packages;
		}

		public void setPackages(List<LookupName> packages) {
			this.packages = packages;
		}
			
		
	}
