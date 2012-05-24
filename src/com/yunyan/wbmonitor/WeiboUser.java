package com.yunyan.wbmonitor;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.net.WeiboException;

public class WeiboUser {
	
	private long userid;
	private String screen_name;
	private String name;
	private String province;
	private String city;
	private String location;
	private String description;
	private String url;
	private String profile_image_url;
	private String domain;
	private String gender;
	private int followers_count;
	private int friends_count;
	private int statuses_count;
	private int favourites_count;
	private Date created_at;
	private boolean following;
	private boolean allow_all_act_msg;
	private String remark;
	private boolean geo_enabled;
	private boolean verified;
	private boolean allow_all_comment;
	private String avatar_large;
	private String verified_reason;
	private boolean follow_me;
	private int online_status;
	private int bi_followers_count;
	
	WeiboUser() {};
	
	WeiboUser(JSONObject obj) throws JSONException, WeiboException {
		userid = obj.getLong("id");
		screen_name = obj.getString("screen_name");
		name = obj.getString("name");
		province = obj.getString("province");
		city = obj.getString("city");
		location = obj.getString("location");
		description = obj.getString("description");
		url = obj.getString("url");
		profile_image_url = obj.getString("profile_image_url");
		domain = obj.getString("domain");
		gender = obj.getString("gender");
		followers_count = obj.getInt("followers_count");
		friends_count = obj.getInt("friends_count");
		statuses_count = obj.getInt("statuses_count");
		favourites_count = obj.getInt("favourites_count");
		created_at = WeiboAPI.parseDate(obj.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
		following  = obj.getBoolean("following");
		allow_all_act_msg = obj.getBoolean("allow_all_act_msg");
		remark = obj.getString("remark");
		geo_enabled = obj.getBoolean("geo_enabled");
		verified = obj.getBoolean("verified");
		allow_all_comment = obj.getBoolean("allow_all_comment");
		avatar_large = obj.getString("avatar_large");
		verified_reason = obj.getString("verified_reason");
		follow_me = obj.getBoolean("follow_me");
		online_status = obj.getInt("online_status");
		bi_followers_count = obj.getInt("bi_followers_count");
	}
	
	public String screen_name() {
		return screen_name;
	}
	
	public long userid() {
		return userid;
	}
}
