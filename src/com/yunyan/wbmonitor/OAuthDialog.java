package com.yunyan.wbmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class OAuthDialog extends Activity implements WeiboDialogListener {

	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
        String token = values.getString("access_token");
        String expires_in = values.getString("expires_in");
        AccessToken accessToken = new AccessToken(token, Weibo.getInstance().getAppSecret());
        accessToken.setExpiresIn(expires_in);
        Weibo.getInstance().setAccessToken(accessToken);
        Intent intent = new Intent();
   //     intent.setClass(oAuthResult.this, oAuthResult.class);
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
