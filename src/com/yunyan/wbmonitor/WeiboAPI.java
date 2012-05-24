package com.yunyan.wbmonitor;

public class WeiboAPI {
static final String public_timeline = "statuses/public_timeline" ;  //	获取最新的公共微博
static final String friends_timeline = "statuses/friends_timeline"; //	获取当前登录用户及其所关注用户的最新微博
static final String home_timeline = "statuses/home_timeline";	//获取当前登录用户及其所关注用户的最新微博" +
		
static final String friends_timeline_ids = "statuses/friends_timeline/ids"; //	获取当前登录用户及其所关注用户的最新微博的ID
//static final String statuses/user_timeline	获取用户发布的微博
//static final String statuses/user_timeline/ids	获取用户发布的微博的ID 
//static final String statuses/repost_timeline	返回一条原创微博的最新转发微博
//static final String statuses/repost_timeline/ids	获取一条原创微博的最新转发微博的ID 
//static final String statuses/repost_by_me	返回用户转发的最新微博
//static final String statuses/mentions	获取@当前用户的最新微博
//static final String statuses/mentions/ids	获取@当前用户的最新微博的ID 
//static final String statuses/bilateral_timeline	获取双向关注用户的最新微博 
//static final String statuses/show	根据ID获取单条微博信息
//static final String statuses/querymid	通过id获取mid
//static final String statuses/queryid	通过mid获取id
//static final String statuses/hot/repost_daily	按天返回热门转发榜
//static final String statuses/hot/repost_weekly	按周返回热门转发榜
//static final String statuses/hot/comments_daily	按天返回热门评论榜
//static final String statuses/hot/comments_weekly	按周返回热门评论榜
//static final String statuses/count	批量获取指定微博的转发数评论数
//static final String 	statuses/repost	转发一条微博信息
//static final String statuses/destroy	删除微博信息
//static final String statuses/update	发布一条微博信息
//static final String statuses/upload	上传图片并发布一条微博
//static final String statuses/upload_url_text	发布一条微博同时指定上传的图片或图片url 
//static final String emotions	获取官方表情
}
