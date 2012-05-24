package com.yunyan.wbmonitor;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.net.WeiboException;

public class WeiboResponse {
	private Date   created_at;
	private long   weiboid;
	private String text;
	private String source;
	
	WeiboUser user;	
	
	WeiboResponse() {};
	
	WeiboResponse(JSONObject json) throws JSONException, WeiboException {
		weiboid = json.getLong("id");
		text = json.getString("text");
		source = json.getString("source");
		user = new WeiboUser(json.getJSONObject("user"));
		created_at = WeiboAPI.parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
	}
	
	public String Text() {
		return text;
	}
}
