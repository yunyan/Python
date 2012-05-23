package com.yunyan.wbmonitor;


import android.app.Activity;
import android.content.Intent;
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

public class WbmonitorActivity extends Activity implements WeiboDialogListener{
    /** Called when the activity is first created. */
	
	private static final String URL_CALLBACK = "wbmonitor://oAuthResult";
	private static final String FROM = "WBMONITOR";
	
	private static final String APPKEY = Weibo.getAppKey();
	private static final String APPSECRET = Weibo.getAppSecret();
	
	
	private OnClickListener mOAuthBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "OAuth", Toast.LENGTH_LONG).show();	
			Weibo wb = oAuthHelper.getWeibo();
			wb.setupConsumerConfig(APPKEY, APPSECRET);
			wb.setRedirectUrl("http://www.weibo.com");
			wb.authorize(WbmonitorActivity.this, WbmonitorActivity.this);
			
//			try{
//				RequestToken rqToken = wb.getRequestToken(getApplicationContext(), APPKEY, APPSECRET, URL_CALLBACK);
//				oAuthHelper.getInstance().setRequestToken(rqToken);
//	
//				Uri uri = Uri.parse(Weibo.URL_AUTHENTICATION + "?display=wap2.0&oauth_token=" + rqToken.getToken() + "&from=" + WbmonitorActivity.FROM);
//				startActivity(new Intent(Intent.ACTION_VIEW, uri));
//			} catch (Exception e) {
//				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//			}	
			
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button OAuthBtn = (Button)findViewById(R.id.oauthbtn);     
        
        OAuthBtn.setOnClickListener(mOAuthBtnListener);   
    }    
    
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
        String token = values.getString("access_token");
        String expires_in = values.getString("expires_in");
        AccessToken accessToken = new AccessToken(token, oAuthHelper.getWeibo().getAppSecret());
        accessToken.setExpiresIn(expires_in);
        Weibo.getInstance().setAccessToken(accessToken);
        Intent intent = new Intent();
        intent.setClass(WbmonitorActivity.this, oAuthResult.class);
       // intent.setClass(,  oAuthResult.class);
        startActivity(intent);
        
	}

	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(),
                Toast.LENGTH_LONG).show();
	}

	public void onError(DialogError e) {
		// TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(),
                Toast.LENGTH_LONG).show();

	}

	public void onCancel() {
		// TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
	}
	
}