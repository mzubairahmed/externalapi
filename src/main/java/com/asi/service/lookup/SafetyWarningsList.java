package com.asi.service.lookup;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.asi.service.lookup.vo.LookupName;

@XmlRootElement(name = "SafetyWarnings")
public class SafetyWarningsList {


		private List<LookupName> safetyWarnings = new ArrayList<>();

		public List<LookupName> getSafetyWarnings() {
			return safetyWarnings;
		}

		public void setSafetyWarnings(List<LookupName> safetyWarnings) {
			this.safetyWarnings = safetyWarnings;
		}
			
		
	}

