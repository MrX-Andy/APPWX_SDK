package com.appwx.sdk.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    private SmartImageTask currentTask;


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    // 设置图像URL
    public void setImageUrl(String url) {
        setImage(new WebImage(url));
    }

    //设置图像URL,成功回调监听
    public void setImageUrl(String url, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), completeListener);
    }

    //设置图像URL,回调res
    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(url), fallbackResource);
    }

    //设置图像URL,回调res,成功回调监听
    public void setImageUrl(String url, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, completeListener);
    }

    //设置图像URL,回调res,加载res
    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(url), fallbackResource, loadingResource);
    }

    //设置图像URL,回调res,加载res,成功回调监听
    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, loadingResource, completeListener);
    }


    // 联系地址簿ID设置图像
    public void setImageContact(long contactId) {
        setImage(new ContactImage(contactId));
    }

    //设置图片，联系人 回调res
    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    //设置图片，联系人 ，回调res，加载res
    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }


    //设置image 为SmartImagec对象
    public void setImage(final SmartImage image) {
        setImage(image, null, null, null);
    }

    public void setImage(final SmartImage image, final SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    /**
     * 设置加载图片
     * @param image url
     * @param fallbackResource 回调res
     * @param loadingResource  加载res
     * @param completeListener 回调成功
     */
    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
        // 设置加载资源
        if(loadingResource != null){
            setImageResource(loadingResource);
        }

        // 取消当前view执行的任务
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // 设置新任务
        currentTask = new SmartImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                } else {
                    // 设置回调res(默认图片)
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete();
                }
            }
        });

        //执行任务的线程池
        threadPool.execute(currentTask);
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
}