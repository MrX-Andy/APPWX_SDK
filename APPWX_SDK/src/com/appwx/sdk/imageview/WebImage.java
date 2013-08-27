package com.appwx.sdk.imageview;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.appwx.sdk.common.Loger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebImage implements SmartImage {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;

    private static WebImageCache webImageCache;

    private String url;

    public WebImage(String url) {
        this.url = url;
    }

    public Bitmap getBitmap(Context context) {
        // Don't leak context
        if(webImageCache == null) {
            webImageCache = new WebImageCache(context);
        }
        // 尝试首先从缓存中得到位图
        Bitmap bitmap = null;
        if(url != null) {
            bitmap = webImageCache.get(url);
            if(bitmap == null) {
                bitmap = getBitmapFromUrl(url);
                if(bitmap != null){
                    webImageCache.put(url, bitmap);
                }
            }
        }
        return bitmap;
    }

    //通过URL获取bitmap
    private Bitmap getBitmapFromUrl(String url) {
    	Loger.log("download url = ", url);
        Bitmap bitmap = null;

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //通过key url 来删除缓存
    public static void removeFromCache(String url) {
        if(webImageCache != null) {
            webImageCache.remove(url);
        }
    }
    
    //清除所有缓存
    public static void clearFromCache(){
    	if(webImageCache != null){
    		webImageCache.clear();
    	}
    }
}
