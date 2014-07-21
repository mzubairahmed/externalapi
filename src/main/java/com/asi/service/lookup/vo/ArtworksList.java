package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
	@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "artworksList")
	public class ArtworksList {
		private List<Artwork> artworks = new ArrayList<Artwork>();

		public List<Artwork> getArtworks() {
			return artworks;
		}

		public void setArtworks(List<Artwork> artworks) {
			this.artworks = artworks;
		}
}
