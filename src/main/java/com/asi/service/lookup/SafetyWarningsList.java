package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SafetyWarnings")
public class SafetyWarningsList {


		private List<String> safetyWarnings = new ArrayList<>();

		public List<String> getSafetyWarnings() {
			return safetyWarnings;
		}

		public void setSafetyWarnings(List<String> safetyWarnings) {
			this.safetyWarnings = safetyWarnings;
		}
			
		
	}

