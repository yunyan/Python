package com.yunyan.wbmonitor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class oAuthResult extends ListActivity {

	private List<WeiboResponse> response = new ArrayList<WeiboResponse>();
	private List<WeiboUser> userlist = new ArrayList<WeiboUser>();
	
	private void getTimeLine(Weibo wb) 	
	throws MalformedURLException, IOException, WeiboException, JSONException{
		String url = wb.SERVER + "statuses/home_timeline.json";
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());
		bundle.add("oauth_verifier", Weibo.getInstance().getAccessToken().getVerifier());
		
		String rlt = wb.request(this, url, bundle, "GET", wb.getAccessToken());		
		
		JSONObject js = new JSONObject(rlt);
		
		JSONArray status = js.getJSONArray("statuses");
		
		for(int i=0; i<status.length(); i++){
			response.add(new WeiboResponse(status.getJSONObject(i)));
		}			
	}
	
	private void getFriends(Weibo wb) throws WeiboException, JSONException {
		String url = wb.SERVER + "friendships/friends.json";
		int cursor = 0;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());
		bundle.add("access_token", Weibo.getInstance().getAccessToken().getToken());
		bundle.add("uid", oAuthHelper.getInstance().getuid());
		bundle.add("cursor", Integer.toString(cursor));
		
		
		do {
			String rlt = wb.request(this, url, bundle, "GET", wb.getAccessToken());		
				
			JSONObject js = new JSONObject(rlt);
			
			cursor = js.getInt("next_cursor");
			bundle.add("cursor", Integer.toString(cursor));
			
			JSONArray userarray = js.getJSONArray("users");
		
			for(int i=0; i<userarray.length(); i++){
				JSONObject user = (JSONObject) userarray.get(i);
				userlist.add(new WeiboUser(user));	
			}	
		
		}while(cursor>0);
	}
	
	private void getUID(Weibo wb) throws WeiboException, JSONException {
		String url = wb.SERVER + "account/get_uid.json";
		
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());

		String rlt = wb.request(this, url, bundle, "GET", wb.getAccessToken());	

		JSONObject json = new JSONObject(rlt);		
		oAuthHelper.getInstance().setuid(json.getString("uid"));				
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		try {
			getUID(Weibo.getInstance());
			getTimeLine(Weibo.getInstance());
			getFriends(Weibo.getInstance());
		} catch (Exception e){
			Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();	
		}
		
		List<String> text = new ArrayList<String>();
		List<String> user_name_list = new ArrayList<String>();
		
		for(WeiboResponse wb: response) {
			text.add(wb.Text());
		}		
		
		for(WeiboUser user: userlist) {
			user_name_list.add(user.screen_name());
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.oauthresult, user_name_list));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}
}
