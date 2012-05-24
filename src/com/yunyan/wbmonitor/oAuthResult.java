package com.yunyan.wbmonitor;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class oAuthResult extends Activity {

	private OnClickListener mGetTimelineClickListner = new OnClickListener() {
		public void onClick(View v) {
			try {
				getTimeLine(Weibo.getInstance());
			}catch (Exception e){
				Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();			
			}
		}
	};
	
	private String getTimeLine(Weibo wb) 
	throws MalformedURLException, IOException, WeiboException{
		String url = wb.SERVER + "statuses/home_timeline.json";
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());
		bundle.add("oauth_verifier", Weibo.getInstance().getAccessToken().getVerifier());
		
		String rlt = wb.request(this, url, bundle, "GET", wb.getAccessToken());		
		
		Toast.makeText(getApplicationContext(), rlt, Toast.LENGTH_LONG).show();
		return rlt;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauthresult);
		
		Button btnGetTimeline = (Button)findViewById(R.id.btn_gettimeline);
		btnGetTimeline.setOnClickListener(mGetTimelineClickListner);
	}
	



}
