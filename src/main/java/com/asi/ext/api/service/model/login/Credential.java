package com.asi.ext.api.service.model.login;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "Credential")
@JsonInclude(Include.NON_NULL)
public class Credential {
	
	@JsonProperty("Asi")
	private String asi;
	
	@JsonProperty("Username")
	private String username;
	
	@JsonProperty("Password")
	private String password;
	
	@XmlElement(name = "Asi")
	public String getAsi() {
		return asi;
	}

	public void setAsi(String asi) {
		this.asi = asi;
	}

	@XmlElement(name = "Username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement(name = "Password")
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
