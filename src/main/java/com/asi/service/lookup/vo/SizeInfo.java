package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "sizeInfo")
public class SizeInfo {
	private List<Size> sizes = new ArrayList<Size>();

	public List<Size> getSizes() {
		return sizes;
	}

	public void setSizes(List<Size> sizes) {
		this.sizes = sizes;
	}
	
}
