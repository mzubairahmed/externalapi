package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "categoriesList")
public class CategoriesList {
	private List<Category> sizes = new ArrayList<Category>();

	public List<Category> getSizes() {
		return sizes;
	}

	public void setSizes(List<Category> sizes) {
		this.sizes = sizes;
	}

}
