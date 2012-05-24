package com.yunyan.wbmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.weibo.net.WeiboException;

public class WeiboAPI {
	
	private static Map<String,SimpleDateFormat> formatMap = new HashMap<String,SimpleDateFormat>();
	
	static final String public_timeline = "statuses/public_timeline" ;  //	��ȡ���µĹ���΢��
	static final String friends_timeline = "statuses/friends_timeline"; //	��ȡ��ǰ��¼�û���������ע�û�������΢��
	static final String home_timeline = "statuses/home_timeline";	//��ȡ��ǰ��¼�û���������ע�û�������΢��" +
		
	static final String friends_timeline_ids = "statuses/friends_timeline/ids"; //	��ȡ��ǰ��¼�û���������ע�û�������΢����ID
//static final String statuses/user_timeline	��ȡ�û�������΢��
//static final String statuses/user_timeline/ids	��ȡ�û�������΢����ID 
//static final String statuses/repost_timeline	����һ��ԭ��΢��������ת��΢��
//static final String statuses/repost_timeline/ids	��ȡһ��ԭ��΢��������ת��΢����ID 
//static final String statuses/repost_by_me	�����û�ת��������΢��
//static final String statuses/mentions	��ȡ@��ǰ�û�������΢��
//static final String statuses/mentions/ids	��ȡ@��ǰ�û�������΢����ID 
//static final String statuses/bilateral_timeline	��ȡ˫���ע�û�������΢�� 
//static final String statuses/show	����ID��ȡ����΢����Ϣ
//static final String statuses/querymid	ͨ��id��ȡmid
//static final String statuses/queryid	ͨ��mid��ȡid
//static final String statuses/hot/repost_daily	���췵������ת����
//static final String statuses/hot/repost_weekly	���ܷ�������ת����
//static final String statuses/hot/comments_daily	���췵���������۰�
//static final String statuses/hot/comments_weekly	���ܷ����������۰�
//static final String statuses/count	������ȡָ��΢����ת����������
//static final String 	statuses/repost	ת��һ��΢����Ϣ
//static final String statuses/destroy	ɾ��΢����Ϣ
//static final String statuses/update	����һ��΢����Ϣ
//static final String statuses/upload	�ϴ�ͼƬ������һ��΢��
//static final String statuses/upload_url_text	����һ��΢��ͬʱָ���ϴ���ͼƬ��ͼƬurl 
//static final String emotions	��ȡ�ٷ�����


public static Date parseDate(String str, String format) throws WeiboException{
	if(str==null||"".equals(str)){
    	return null;
    }
	SimpleDateFormat sdf = formatMap.get(format);
    if (null == sdf) {
        sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        formatMap.put(format, sdf);
    }
    try {
        synchronized(sdf){
            // SimpleDateFormat is not thread safe
            return sdf.parse(str);
        }
    } catch (ParseException pe) {
        throw new WeiboException("Unexpected format(" + str + ") returned from sina.com.cn");
    }
}	
}
