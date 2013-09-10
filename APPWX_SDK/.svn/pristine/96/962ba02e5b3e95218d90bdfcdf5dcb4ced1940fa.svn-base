package com.appwx.sdk.imageview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64InputStream;

import com.appwx.sdk.common.Loger;
import com.appwx.sdk.md5.MD5;

public class WebImageCache {
    private static final String DISK_CACHE_PATH = "/web_image_cache/";//SD卡缓存路径

    private ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache;
    private String diskCachePath;
    private boolean diskCacheEnabled = false;
    private ExecutorService writeThread;

    public WebImageCache(Context context) {
        // 在内存缓存存储
        memoryCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

        // 设置SD卡缓存存储
        Context appContext = context.getApplicationContext();
        
        //图片所缓存的绝对路径
        //data/data/com.appwx.sdk.demo/web_image_cache/http+www+zhaoshang800+com+images+yuanqu+pic+201305301548322877+jpg
//        diskCachePath = appContext.getCacheDir().getAbsolutePath() + DISK_CACHE_PATH;
        //storage/sdcard0/com.appwx.sdk.demo/web_image_cache/http+www+zhaoshang800+com+images+yuanqu+pic+201305301548322877+jpg
        diskCachePath = Environment.getExternalStorageDirectory() + "/com.appwx.sdk.demo" +  DISK_CACHE_PATH;

        File outFile = new File(diskCachePath);
        outFile.mkdirs();

        //文件夹是否存在
        diskCacheEnabled = outFile.exists();

        // 线程池设置​​的图像抓取任务
        writeThread = Executors.newSingleThreadExecutor();
    }

    public Bitmap get(final String url) {
    	Loger.log("Cache url = ", url);
        Bitmap bitmap = null;

        // 检查图像存储在内存
        bitmap = getBitmapFromMemory(url);

        // 检查SD卡上缓存的图像
        if(bitmap == null) {
            bitmap = getBitmapFromDisk(url);

            // 把bitmap写入缓存中
            if(bitmap != null) {
                cacheBitmapToMemory(url, bitmap);
            }
        }

        return bitmap;
    }

    /**
     * put 软引用中memoryCache
     * @param url 为下载时的url
     * @param bitmap 下载完的bitmap对象
     */
    public void put(String url, Bitmap bitmap) {
        cacheBitmapToMemory(url, bitmap);//缓存内存
        cacheBitmapToDisk(url, bitmap);//缓存SD卡
    }

    //通过url 删除memoryCache或SD卡中的缓存
    public void remove(String url) {
        if(url == null){
            return;
        }

        // 从内存缓存中删除
        memoryCache.remove(getCacheKey(url));

        // 从文件缓存中删除
        File f = new File(diskCachePath, getCacheKey(url));
        if(f.exists() && f.isFile()) {
            f.delete();
        }
    }

    //删除memoryCache和SD卡中的缓存及所有缓存
    public void clear() {
        // 从内存缓存中删除一切
    	memoryCache.clear();

        // 删除一切从文件缓存
        File cachedFileDir = new File(diskCachePath);
        if(cachedFileDir.exists() && cachedFileDir.isDirectory()) {
            File[] cachedFiles = cachedFileDir.listFiles();
            for(File f : cachedFiles) {
                if(f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    //缓存到软引用中
    private void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
        memoryCache.put(getCacheKey(url), new SoftReference<Bitmap>(bitmap));
    }

    //缓存到SD卡
    private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
        writeThread.execute(new Runnable() {
            @Override
            public void run() {
                if(diskCacheEnabled) {
                    BufferedOutputStream ostream = null;
                    try {
                        ostream = new BufferedOutputStream(new FileOutputStream(new File(diskCachePath, getCacheKey(url))), 2*1024);
                        bitmap.compress(CompressFormat.PNG, 100, ostream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(ostream != null) {
                                ostream.flush();
                                ostream.close();
                            }
                        } catch (IOException e) {}
                    }
                }
            }
        });
    }

    //存储在内存中的图片
    private Bitmap getBitmapFromMemory(String url) {
    	Loger.log("Memory url", url);
        Bitmap bitmap = null;
        SoftReference<Bitmap> softRef = memoryCache.get(getCacheKey(url));
        if(softRef != null){
            bitmap = softRef.get();
        }

        return bitmap;
    }

    //存储在SD卡中的图片
    private Bitmap getBitmapFromDisk(String url) {
    	Loger.log("SD url", url);
        Bitmap bitmap = null;
        if(diskCacheEnabled){
            String filePath = getFilePath(url);
            File file = new File(filePath);
            if(file.exists()) {
                bitmap = BitmapFactory.decodeFile(filePath);
            }
        }
        return bitmap;
    }

    //获取图片所在SD卡路径
    private String getFilePath(String url) {
        return diskCachePath + getCacheKey(url);
    }

    private String getCacheKey(String url) {
        if(url == null){
            throw new RuntimeException("Invalid url");
        } else {
            return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
    }
    
    
}
