package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "artworks")
public class ArtworksList {
		private List<String> artworks = new ArrayList<>();

		public List<String> getArtworks() {
			return artworks;
		}

		public void setArtworks(List<String> artworks) {
			this.artworks = artworks;
		}

}