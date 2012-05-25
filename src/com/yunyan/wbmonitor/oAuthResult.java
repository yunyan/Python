package com.yunyan.wbmonitor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class oAuthResult extends ListActivity {
	
	private ListView mlv;
	private ListActivity mContext;
	ProgressDialog dialog;
	List<WeiboUser> mUserlist = null;
	
	class GetUserListTask extends AsyncTask<ListActivity, Integer, List<WeiboUser>> 
	{
		
		private List<WeiboResponse> getTimeLine(Context context, Weibo wb) 	
		throws MalformedURLException, IOException, WeiboException, JSONException{
			List<WeiboResponse> response = new ArrayList<WeiboResponse>();
			String url = Weibo.SERVER + "statuses/home_timeline.json";
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", Weibo.getAppKey());
			bundle.add("oauth_verifier", Weibo.getInstance().getAccessToken().getVerifier());
			
			String rlt = wb.request(context, url, bundle, "GET", wb.getAccessToken());		
			
			JSONObject js = new JSONObject(rlt);
			
			JSONArray status = js.getJSONArray("statuses");
			
			for(int i=0; i<status.length(); i++){
				response.add(new WeiboResponse(status.getJSONObject(i)));
			}			
			
			return response;
		}
		
		private List<WeiboUser> getFriends(Context context, Weibo wb) throws WeiboException, JSONException {
			List<WeiboUser> userlist = new ArrayList<WeiboUser>();
			String url = Weibo.SERVER + "friendships/friends.json";
			int cursor = 0;
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", Weibo.getAppKey());
			bundle.add("access_token", Weibo.getInstance().getAccessToken().getToken());
			bundle.add("uid", oAuthHelper.getInstance().getuid());
			bundle.add("cursor", Integer.toString(cursor));
			
			
			do {
				String rlt = wb.request(context, url, bundle, "GET", wb.getAccessToken());		
					
				JSONObject js = new JSONObject(rlt);
				
				cursor = js.getInt("next_cursor");
				bundle.add("cursor", Integer.toString(cursor));
				
				JSONArray userarray = js.getJSONArray("users");
			
				for(int i=0; i<userarray.length(); i++){
					JSONObject user = (JSONObject) userarray.get(i);
					userlist.add(new WeiboUser(user));	
					publishProgress(i);
				}					
			}while(cursor>0);
			
			return userlist;
		}
		
		private void getUID(Context context, Weibo wb) throws WeiboException, JSONException {
			String url = Weibo.SERVER + "account/get_uid.json";
			
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", Weibo.getAppKey());

			String rlt = wb.request(context, url, bundle, "GET", wb.getAccessToken());	

			JSONObject json = new JSONObject(rlt);		
			oAuthHelper.getInstance().setuid(json.getString("uid"));				
		}
		
		@Override
		protected List<WeiboUser> doInBackground(ListActivity... arg0) {
			// TODO Auto-generated method stub
			Log.d("iAsycTask", "doInBackground");
			List<WeiboUser> userlist = null;
			
			try {
				getUID(arg0[0], Weibo.getInstance());
				userlist = getFriends(arg0[0], Weibo.getInstance());
			} catch (Exception e){
				e.printStackTrace();	
			}
			
			return userlist;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(List<WeiboUser> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("iAsycTask", "onPostExecute");	
			
			List<String> user_name_list = new ArrayList<String>();
			

			for(WeiboUser user: result) {
				user_name_list.add(user.screen_name());
			}
			
			mlv.setAdapter(new ArrayAdapter<String>(mContext, R.layout.oauthresult, user_name_list));
			dialog.dismiss();
			
			mUserlist = result;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(mContext);
			dialog.setMessage("Retrieving friends from Weibo....");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			dialog.show();				
		}
	}

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = this;
		
		GetUserListTask task = new GetUserListTask();
		task.execute(this);		
	
		mlv = getListView();
		mlv.setTextFilterEnabled(true);
		
		mlv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent i = new Intent();
				i.setClass(mContext, OperationActivity.class);
				i.putExtra("position", position);
				i.putExtra("screen_name", mUserlist.get(position).screen_name());
				i.putExtra("id", mUserlist.get(position).userid());				
				startActivity(i);
			}
		});
	
	}
}
