package com.yunyan.wbmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;

import com.weibo.net.WeiboException;

public class WeiboAPI {

	private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();
	


	public static Date parseDate(String str, String format)
			throws WeiboException {
		if (str == null || "".equals(str)) {
			return null;
		}
		SimpleDateFormat sdf = formatMap.get(format);
		if (null == sdf) {
			sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			formatMap.put(format, sdf);
		}
		try {
			synchronized (sdf) {
				// SimpleDateFormat is not thread safe
				return sdf.parse(str);
			}
		} catch (ParseException pe) {
			throw new WeiboException("Unexpected format(" + str
					+ ") returned from sina.com.cn");
		}
	}
}
