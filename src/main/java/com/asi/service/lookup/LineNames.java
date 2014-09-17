package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "linenames")
public class LineNames {
	private List<String> linenames = new ArrayList<>();

	public List<String> getLinenames() {
		return linenames;
	}

	public void setLineNames(List<String> linenames) {
		this.linenames = linenames;
	}

}
