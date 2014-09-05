package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Materials")
public class MaterialsList {


		private List<String> materials = new ArrayList<>();

		public List<String> getMaterials() {
			return materials;
		}

		public void setMaterials(List<String> materials) {
			this.materials = materials;
		}
			
		
	}

