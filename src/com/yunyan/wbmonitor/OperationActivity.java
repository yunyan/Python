package com.yunyan.wbmonitor;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
	private boolean mRecordExist = false;
	
	private EditText mEdtForward = null;
	private EditText mEdtComment = null;
	private TextView mTxtNotice = null;
	private CheckBox mChkComment = null;
	private CheckBox mChkForward = null;
	private CheckBox mChkDelete = null;
	private Button mBtnConfirm = null;
	private OperationDBHelper mDbhelper = null;
	
	private static JSONObject mOperationObject = null;
	
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

	private OnCheckedChangeListener mDeleteChangeListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
			if(isChecked){
				//mEdtForward.setText("");
				mEdtForward.setEnabled(!isChecked);
				//mEdtComment.setText("");
				mEdtComment.setEnabled(!isChecked);
				mChkComment.setChecked(!isChecked);
				mChkForward.setChecked(!isChecked);
			}
			

//			mEdtForward.setEnabled(!isChecked);
//			mEdtComment.setEnabled(!isChecked);
//			mChkComment.setChecked(!isChecked);
//			mChkForward.setChecked(!isChecked);
		
		}
	};
	
	private void printOperation(){
		JSONObject op = null;	

		try {
			// long id = json.getLong("id");
			String name = mOperationObject.getString("name");
			op = mOperationObject.getJSONObject("Operation");
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

			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
			Log.i("printOperation", msg);
		} catch (JSONException e) {
			Log.e("printOperation", e.toString());
		}

	}
	
	private OnClickListener mBtnConfirmListener = new OnClickListener() {
		public void onClick(View v) {
			SQLiteDatabase db = mDbhelper.getWritableDatabase();
			String sql = "";
			String comment = mChkComment.isChecked()?"1":"0";
			String forward = mChkForward.isChecked()?"1":"0";
			
			try {
				
				if (mChkDelete.isChecked()){
					sql += "delete from " + OperationDBHelper.mTablename + " where id = " + mId;
				} else if(mRecordExist == false) {
					sql += "insert into " + OperationDBHelper.mTablename + " values (" + mId + "," + "\"" + mScreen_name + "\"" + "," +
							comment + "," + "\"" +
							mEdtComment.getText() + "\"" + "," +
							forward + "," + "\"" +
							mEdtForward.getText() + "\"" + ");";
					
				}else{
					sql +="update " + OperationDBHelper.mTablename + " set " +
						//	"id = " + mId + "," + "name = " + mScreen_name + "," +
							"comment = " + comment + "," +
							"comment_content = " + "\"" + mEdtComment.getText() +"\"" + "," +
							"forward = " + forward + "," +
							"forward_content = " + "\"" + mEdtForward.getText() + "\"" +";" ;
									
				}
				
				
				db.execSQL(sql);
				
			} catch (SQLException e) {
				Log.e("SQLite", e.getMessage());
			}
			
			db.close();
			finish();
		}
	};
	
	private void initStatus(Intent i) {
		
		JSONObject op = null;	
		mEdtForward.clearFocus();
		mEdtComment.clearFocus();
		mEdtForward.setEnabled(false);
		mEdtComment.setEnabled(false);	

		try {
			String name = mOperationObject.getString("name");
			op = mOperationObject.getJSONObject("Operation");
			boolean comment = op.getBoolean("Comment");
			String commentContent = op.getString("Commentcontent");
			boolean forward = op.getBoolean("Forward");
			String forwardContent = op.getString("Forwardcontent");
			
			mEdtForward.setEnabled(forward);
			mEdtComment.setEnabled(comment);				
			mEdtForward.setText(forwardContent);			
			mEdtComment.setText(commentContent);
			mChkForward.setChecked(forward);
			mChkComment.setChecked(comment);

		} catch (JSONException e) {
			Log.i("initStatus", e.getMessage());		
		}
	}
	
	private void initOperationObject(Intent i) {
		mOperationObject = new JSONObject();
		SQLiteDatabase db = mDbhelper.getWritableDatabase();	
		Cursor c = null;
		
		String[] colums = new String[]{"comment", "comment_content", "forward", "forward_content"};
		try {
			c = db.query(OperationDBHelper.mTablename, 
					colums,
					"id = "+ mId,
					null, null, null, null);		
		} catch (SQLException  e) {
			Log.e("SQLite","Execute Query: " + e.getMessage());
		}
		
		if(c != null && c.getCount()>0) {
			try {				
				mRecordExist = true;
				mOperationObject.put("id", mId);
				mOperationObject.put("name", mScreen_name);

				JSONObject op = new JSONObject();
				if(c.moveToFirst()) {
					op.put("Comment",
							(c.getInt(c.getColumnIndex("comment")) == 0) ? false
									: true);
					op.put("Commentcontent",
							c.getString(c.getColumnIndex("comment_content")));
					op.put("Forward",
							(c.getInt(c.getColumnIndex("forward")) == 0) ? false
									: true);
					op.put("Forwardcontent",
							c.getString(c.getColumnIndex("forward_content")));
				}

				mOperationObject.put("Operation", op);
				
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("InitOperation", e.getMessage());
			}
		} else {
			mRecordExist = false;
		}
		c.close();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operation);
		
		mDbhelper = new OperationDBHelper(this, OperationDBHelper.mDbPath + OperationDBHelper.mDbName, null, OperationDBHelper.DATABASE_VERSION);
		
		mEdtForward = (EditText)findViewById(R.id.edt_forward);
		mEdtComment = (EditText)findViewById(R.id.edt_comment);
		mTxtNotice = (TextView)findViewById(R.id.txt_notice);
		mChkComment = (CheckBox)findViewById(R.id.chkbox_comment);
		mChkForward = (CheckBox)findViewById(R.id.chkbox_forward);
		mBtnConfirm = (Button)findViewById(R.id.btn_confirm);		
		mChkDelete = (CheckBox)findViewById(R.id.chkbox_remove);
		
		Intent i = getIntent();
		mScreen_name = i.getExtras().getString("screen_name");
		mId = i.getExtras().getLong("id");
		mTxtNotice.setText("请设定对 " + mScreen_name + " 所发微博的操作");
		
		initOperationObject(i);
		initStatus(i);
		
		mChkComment.setOnCheckedChangeListener(mCommentChangeListner);
		mChkForward.setOnCheckedChangeListener(mForwardChangeListener);
		mChkDelete.setOnCheckedChangeListener(mDeleteChangeListener);
		mBtnConfirm.setOnClickListener(mBtnConfirmListener);		
	}	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
