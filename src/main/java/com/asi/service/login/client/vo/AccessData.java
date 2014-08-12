package com.asi.service.login.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "AccessToken",
    "TokenExpirationTime",
})

public class AccessData {
	
	@JsonProperty("AccessToken")
    private String accessToken;
	
	@JsonProperty("TokenExpirationTime")
    private String tokenExpirationTime;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(String tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}
	
	
	

}
