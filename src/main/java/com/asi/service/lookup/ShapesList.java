package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Shapes")
public class ShapesList {


		private List<String> shapes = new ArrayList<>();

		public List<String> getShapes() {
			return shapes;
		}

		public void setShapes(List<String> shapes) {
			this.shapes = shapes;
		}
			
		
	}

