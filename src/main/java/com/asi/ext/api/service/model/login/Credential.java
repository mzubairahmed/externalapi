package com.asi.ext.api.service.model.login;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Credential {
	
	@JsonProperty("Asi")
	private String asi;
	
	@JsonProperty("Username")
	private String username;
	
	@JsonProperty("Password")
	private String password;

	public String getAsi() {
		return asi;
	}

	public void setAsi(String asi) {
		this.asi = asi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return "[ Asi = " + getAsi() + ", Username = " + getUsername() + ", Password = " + getPassword() + " ]";
	}
	
}
