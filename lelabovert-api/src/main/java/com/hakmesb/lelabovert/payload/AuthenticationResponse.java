package com.hakmesb.lelabovert.payload;

public class AuthenticationResponse {
	
	private String accessToken;
	private String refreshToken;
	
	public AuthenticationResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

}
