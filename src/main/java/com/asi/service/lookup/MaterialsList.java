package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.lookup.vo.LookupName;

@XmlRootElement(name = "Materials")
public class MaterialsList {


		private List<LookupName> materials = new ArrayList<>();

		public List<LookupName> getMaterials() {
			return materials;
		}

		public void setMaterials(List<LookupName> materials) {
			this.materials = materials;
		}
			
		
	}

