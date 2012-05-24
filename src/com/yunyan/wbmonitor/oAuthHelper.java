package com.yunyan.wbmonitor;

import com.weibo.net.AccessToken;
import com.weibo.net.RequestToken;
import com.weibo.net.Weibo;

public class oAuthHelper {
	private static Weibo wb = null;
	private static oAuthHelper oauthinstance = null;
	private RequestToken reqToken = null;
	private AccessToken acToken = null;
	private String token;
	private String tokenSecret;
	
	private static String uid;
	
	private oAuthHelper() {};
	
	public void setuid(String id) {
		uid = id;
	}
	
	public String getuid(){
		return uid;
	}
	
	public static synchronized oAuthHelper getInstance() {
		if (oauthinstance == null){
			oauthinstance = new oAuthHelper();
		}
		return oauthinstance;
	}
	
	public AccessToken getAccessToken() {
		return acToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.acToken = accessToken;
		this.token=accessToken.getToken();
		this.tokenSecret=accessToken.getSecret();
	}
	public RequestToken getRequestToken() {
		return reqToken;
	}
	
	public void setRequestToken(RequestToken requestToken) {
		this.reqToken = requestToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
}
