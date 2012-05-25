package com.yunyan.wbmonitor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OperationActivity extends Activity {
	
	private int mPosition;
	private String mScreen_name;
	private long mId;
	
	private EditText mEdtForward = null;
	private EditText mEdtComment = null;
	private TextView mTxtNotice = null;
	private CheckBox mChkComment = null;
	private CheckBox mChkForward = null;
	private Button mBtnConfirm = null;
	
	private static List<JSONObject> mOperationList = new ArrayList<JSONObject>();
	
	private OnCheckedChangeListener mCommentChangeListner = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
			mEdtComment.setEnabled(isChecked);
			if(isChecked == true) {
				mEdtComment.requestFocus();
			}
			if (isChecked == false) {
				mEdtComment.clearFocus();
				mEdtComment.setText("");
			}
		}
	};
	
	private OnCheckedChangeListener mForwardChangeListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
			mEdtForward.setEnabled(isChecked);
			if(isChecked == true) {
				mEdtForward.requestFocus();
			}
			if (isChecked == false) {
				mEdtForward.clearFocus();
				mEdtForward.setText("");
			}
		}
	};

	private void printOperation(){
		JSONObject json = null;
		JSONObject op = null;
			
		for(int i = 0; i<mOperationList.size(); i++){
			json = mOperationList.get(i);
			try {
				//long id = json.getLong("id");
				String name = json.getString("name");
				op = json.getJSONObject("Operation");
				boolean comment = op.getBoolean("Comment");
				String commentContent = op.getString("Commentcontent");
				boolean forward = op.getBoolean("Forward");
				String forwardContent = op.getString("Forwardcontent");
				
				String msg = "The Operate to " + name + " include:";
				
				if (comment == true)
					msg += " Comment with " + commentContent;
				if (forward == true) 					
					msg += ", Forward with " + forwardContent;
				
				msg += ".";
				
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
				Log.i("printOperation", msg);
			} catch (JSONException e){
				Log.e("printOperation", e.toString());
			}
		}
	}
	
	private OnClickListener mBtnConfirmListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				JSONObject json = new JSONObject();
				json.put("id", mId);
				json.put("name", mScreen_name);
				
				JSONObject op = new JSONObject();
				op.put("Comment", mChkComment.isChecked());
				op.put("Commentcontent", mEdtComment.getText());				
				op.put("Forward", mChkForward.isChecked());
				op.put("Forwardcontent", mEdtForward.getText());			

				json.put("Operation", op);
				
				mOperationList.add(json);
				
				printOperation();
				
			} catch (JSONException e) {
				Log.e("Create operation list", e.toString());
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operation);
		
		mEdtForward = (EditText)findViewById(R.id.edt_forward);
		mEdtComment = (EditText)findViewById(R.id.edt_comment);
		mTxtNotice = (TextView)findViewById(R.id.txt_notice);
		mChkComment = (CheckBox)findViewById(R.id.chkbox_comment);
		mChkForward = (CheckBox)findViewById(R.id.chkbox_forward);
		mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
		
		mEdtForward.setEnabled(false);
		mEdtComment.setEnabled(false);
		mEdtForward.clearFocus();
		mEdtComment.clearFocus();
		
		Intent i = getIntent();
		
		mScreen_name = i.getExtras().getString("screen_name");
		mId = i.getExtras().getLong("id");
		mTxtNotice.setText("请设定对 " + mScreen_name + " 所发微博的操作");
		
		mChkComment.setOnCheckedChangeListener(mCommentChangeListner);
		mChkForward.setOnCheckedChangeListener(mForwardChangeListener);
		mBtnConfirm.setOnClickListener(mBtnConfirmListener);		
	}	

}
