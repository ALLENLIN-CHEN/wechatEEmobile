package com.util;

import java.util.Date;

public class AccessToken {
	private String token;
	private int expiresIn;	//有效时间，两个小时
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", expiresIn=" + expiresIn + "]";
	}


	private String accessToken;
	private Date createTime;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
