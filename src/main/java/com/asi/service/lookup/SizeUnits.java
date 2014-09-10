package com.asi.service.lookup;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "sizes")
@JsonInclude(Include.NON_NULL)
public class SizeUnits {
		private List<String> sizes = null;
		
		private List<SizeType> sizeUnits = null;
		
		public List<SizeType> getSizeUnits() {
			return sizeUnits;
		}

		public void setSizeUnits(List<SizeType> units) {
			this.sizeUnits = units;
		}

		public List<String> getSizes() {
			return sizes;
		}

		public void setSizes(List<String> sizes) {
			this.sizes = sizes;
		}

}
