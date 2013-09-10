
package com.appwx.sdk.common;

import android.util.Log;


/**
 * DLoger.java
 * 2012-8-31 上午10:53:18
 * TODO
 * @X3Daniel
 */
public class Loger{
	public static boolean debugAble = true;
	public static String TAG = "SDK_LOG";
	
	public static void log(String msg){
		if(debugAble){
			Log.v(TAG, msg);
		}
	}
	public static void log(String msg ,Throwable e){
		if(debugAble){
			Log.w(TAG, msg , e);
		}
	}
	public static void log(String tag,String msg ,Throwable e){
		if(debugAble){
			Log.w(tag, msg , e);
		}
	}
	public static void log(String tag,String msg){
		if(debugAble){
			Log.v(tag, msg);
		}
	}
}



