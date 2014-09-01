package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "themes")
public class ThemesList {
	private List<LookupName> themes = new ArrayList<>();

	public List<LookupName> getThemes() {
		return themes;
	}

	public void setThemes(List<LookupName> themes) {
		this.themes = themes;
	}
	

}
