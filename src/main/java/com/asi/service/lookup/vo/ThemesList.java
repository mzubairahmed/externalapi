package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "themes")
public class ThemesList {
	private List<String> themes = new ArrayList<>();

	public List<String> getThemes() {
		return themes;
	}

	public void setThemes(List<String> themes) {
		this.themes = themes;
	}
	

}
