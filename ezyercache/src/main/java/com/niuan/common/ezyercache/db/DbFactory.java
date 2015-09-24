package com.niuan.common.ezyercache.db;

import android.util.Log;

public class DbFactory {

	private static final String LOG_TAG = DbFactory.class.getName();
	
	public static enum DbType{CARD, INNER};
	
	private static DbCard dbCard;
	
	private static DbInner dbInner;
	
	public static Database getInstance(){
		return getInstance( DbType.INNER );
	}
	
	public static Database getInstance(DbType type){
		if( type == DbType.CARD ){
			synchronized( DbCard.class ){
				if( null == dbCard ){
					dbCard = new DbCard();
				}
				
				return dbCard;
			}
		}
		
		synchronized( DbInner.class ){
			if( null == dbInner ){	
				dbInner = new DbInner();
			}
			
			return dbInner;
		}
	}
	
	public static void closeDataBase(){
		try{
			if( null != dbInner )	dbInner.close();
			if( null != dbCard )	dbCard.close();
			
			dbInner = null;
			dbCard = null;
			
		}
		catch(Exception ex){
			Log.e(LOG_TAG, "closeDabaBase" + ex);
		}
	}
}
