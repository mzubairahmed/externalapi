package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.lookup.vo.LookupName;

@XmlRootElement(name = "Shapes")
public class ShapesList {


		private List<LookupName> shapes = new ArrayList<>();

		public List<LookupName> getShapes() {
			return shapes;
		}

		public void setShapes(List<LookupName> shapes) {
			this.shapes = shapes;
		}
			
		
	}

