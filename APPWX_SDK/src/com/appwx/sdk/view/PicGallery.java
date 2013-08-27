package com.appwx.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class PicGallery extends Gallery {
    public PicGallery(Context paramContext){
        super(paramContext);
     }

     public PicGallery(Context paramContext, AttributeSet paramAttributeSet){
         super(paramContext, paramAttributeSet);
     }

     public PicGallery(Context paramContext, AttributeSet paramAttributeSet, int paramInt){
         super(paramContext, paramAttributeSet, paramInt);
     }

     private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2)
     {  
         return e2.getX() > e1.getX();
     }
       
     
     // 用户按下触摸屏�?快�?移动后松�?
     public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2){
         int kEvent;
         if(isScrollingLeft(paramMotionEvent1, paramMotionEvent2)){
             kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
         } else{
              kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
         }
         onKeyDown(kEvent, null);
         return true;
      }

}
