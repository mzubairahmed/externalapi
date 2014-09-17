package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "fobpoints")
public class FobPoints {
	private List<String> fobpoints = new ArrayList<>();

	public List<String> getFobpoints() {
		return fobpoints;
	}

	public void setFobpoints(List<String> fobpoints) {
		this.fobpoints = fobpoints;
	}

}
