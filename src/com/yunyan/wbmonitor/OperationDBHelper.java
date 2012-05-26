package com.yunyan.wbmonitor;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OperationDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	public static final String mDbPath = "";
	public static final String mDbName = "weibodb.db";
	public static final String mTablename = "operation";
	private static final String mCreateSQL = "CREATE TABLE IF NOT EXISTS " + "\"" +
				mTablename + "\"" + 
				" (id INTEGER PRIMARY KEY, " +
				"name TEXT, " +
				"comment INTEGER, " +
				"comment_content TEXT," +
				"forward INTEGER, " +
				"forward_content TEXT);";
	private Context mContext = null;
			
	OperationDBHelper(Context context, String dbname, CursorFactory factory, int version) {
		super(context, dbname, factory, version );	
		mContext = context;
		}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		Log.d("SQLite", "onCreate");
		try {
			Log.d("SQLite", mCreateSQL);
			arg0.execSQL(mCreateSQL);
		} catch (SQLException  e) {
			Log.e("SQLite", "Create table: " + e.getMessage());
			arg0.close();
			mContext.deleteDatabase(mDbPath + mDbName);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
