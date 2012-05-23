package com.yunyan.wbmonitor;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.weibo.net.RequestToken;
import com.weibo.net.Weibo;

public class WbmonitorActivity extends Activity{
    /** Called when the activity is first created. */
	
	private static final String URL_CALLBACK = "wbmonitor://oAuthResult";
	private static final String FROM = "WBMONITOR";
	
	private static final String APPKEY = Weibo.getAppKey();
	private static final String APPSECRET = Weibo.getAppSecret();
	
	
	private OnClickListener mOAuthBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "OAuth", Toast.LENGTH_LONG).show();	
			Weibo wb = oAuthHelper.getWeibo();
			
			try{
				RequestToken rqToken = wb.getRequestToken(getApplicationContext(), APPKEY, APPSECRET, URL_CALLBACK);
				oAuthHelper.getInstance().setRequestToken(rqToken);
	
				Uri uri = Uri.parse(Weibo.URL_AUTHENTICATION + "?display=wap2.0&oauth_token=" + rqToken.getToken() + "&from=" + WbmonitorActivity.FROM);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}	
			
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button OAuthBtn = (Button)findViewById(R.id.oauthbtn);     
        
        OAuthBtn.setOnClickListener(mOAuthBtnListener);   
    }    
}