package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.lookup.vo.LookupName;

@XmlRootElement(name = "Colors")
public class ColorsList {

	private List<LookupName> colors = new ArrayList<>();

	public List<LookupName> getColors() {
		return colors;
	}

	public void setColors(List<LookupName> colors) {
		this.colors = colors;
	}
	
}
