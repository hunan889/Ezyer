package com.niuan.common.ezyercache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yy.android.gamenews.Constants;
import com.yy.android.gamenews.GameNewsApplication;
import com.yy.android.gamenews.util.Util;

public class DbInner extends Database {

	@Override
	public void init() {
		if (core == null || !core.isOpen()) {
			InnerHelper mHelper = new InnerHelper(GameNewsApplication.getInstance());
			try{
				core = mHelper.getWritableDatabase();
			}catch(SQLiteException ex)
			{
				Log.e("dbinner", "getWritableDatabase" + Util.getStackTraceString(ex));	
			}
		}
	}

	private class InnerHelper extends SQLiteOpenHelper {
		private static final String LOG_TAG = "InnerHelper";

		public InnerHelper(Context context) {
			super(context, Constants.INNER_DATABASE_NAME, null, Constants.INNER_DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			try {
				db.execSQL("create table if not exists t_page_cache(id varchar(50) primary key, content TEXT, row_create_time INTEGER, row_expire_time INTEGER)");
			} catch (Exception ex) {
				Log.e(LOG_TAG, "onCreate|page_cache|" + Util.getStackTraceString(ex));
				;
			}

			try {
				db.execSQL("create table if not exists t_login(uin INTEGER primary key, " +
						"nick_name varchar(30), row_create_time INTEGER)");
			} catch (Exception ex) {
				Log.e(LOG_TAG, "onCreate|t_login|" + Util.getStackTraceString(ex));
				;
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {

	/*		try {
				db.execSQL("DROP TABLE IF EXISTS page_cache");
			} catch (Exception ex) {
				Log.e(LOG_TAG, "onUpgrade|page_cache|" + Util.getStackTraceString(ex));
				;
			}
*/
			
			if( newversion > oldversion ){
				try {
					db.execSQL("DROP TABLE IF EXISTS t_login");
				} catch (Exception ex) {
					Log.e(LOG_TAG, "onUpgrade|t_login|" + Util.getStackTraceString(ex));
					;
				}
	
				onCreate(db);
			}
		}
	}
}
