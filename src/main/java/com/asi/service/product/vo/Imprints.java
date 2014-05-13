package com.asi.service.product.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "imprints")
public class Imprints {
	@XmlElement(name="imprintMethod")
	private List<ImprintMethod> imprintMethods;
    public List<ImprintMethod> getImprintMethod() {
		return imprintMethods;
	}

	public void setImprintMethod(List<ImprintMethod> imprintMethods) {
		this.imprintMethods = imprintMethods;
	}

}
