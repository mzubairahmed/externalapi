package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public class SelectedComplianceCerts {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getComplianceCertId() {
		return complianceCertId;
	}
	public void setComplianceCertId(String complianceCertId) {
		this.complianceCertId = complianceCertId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	@JsonProperty("ID")
	private String id="0";
	@JsonProperty("ProductId")
	private String productId="0";
	@JsonProperty("ComplianceCertId")
	private String complianceCertId="";
	@JsonProperty("Description")
	private String description="";
	@JsonProperty("CompanyId")
	private String companyId="";
}
