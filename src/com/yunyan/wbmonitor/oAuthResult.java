package com.yunyan.wbmonitor;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.weibo.net.RequestToken;

public class oAuthResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauthresult);
		
		Uri uri = this.getIntent().getData();			
		


	}

}
