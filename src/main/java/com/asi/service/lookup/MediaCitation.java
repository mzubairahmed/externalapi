package com.asi.service.lookup;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "mediacitation")
public class MediaCitation {
	private List<String> mediaCitationList = new ArrayList<>();

	public List<String> getMediaCitation() {
		return mediaCitationList;
	}

	public void setMediaCitation(List<String> mediaCitationList) {
		this.mediaCitationList = mediaCitationList;
	}

}
