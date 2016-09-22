package com.wei.mobileoffice.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.wei.mobileoffice.R;


public class TipToast {private static final int MESSAGE_TIMEOUT = 2;
    private WindowManager wdm;
    private double time;
    private static View mView;
    private WindowManager.LayoutParams params;
    private WorkerHandler mHandler;
    private static Toast toast;

    private TipToast(Context context, String text, double time,int offX,int offY) {
        wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mHandler = new WorkerHandler();

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT,offX+toast.getView().getWidth()/2,offY+200);
        mView = toast.getView();

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = R.style.ToastAnimation; //toast.getView().getAnimation().INFINITE;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.x = offX;
        params.y = offY;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        this.time = time;
    }

    public static TipToast makeText(Context context, String text, double time,int offX,int offY) {
        TipToast toastCustom = new TipToast(context, text, time,offX,offY);
        return toastCustom;
    }

    private boolean hasShown = false;
    public void setText(String str){
        toast.setText(str);
        mView = toast.getView();
    }

    public void setGravity(int offX, int offY){
        toast.setGravity(Gravity.TOP | Gravity.LEFT,offX+toast.getView().getWidth()/2,offY);;
        mView = toast.getView();
    }

    public void show() {
        if(hasShown){
            mHandler.removeCallbacksAndMessages(null);
        }else{
            wdm.addView(mView, params);
        }
        mHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT, (long) (time * 1000));
        hasShown = true;
    }

    public boolean isHasShown(){
        return hasShown;
    }

    public void cancel() {
        hasShown = false;
        wdm.removeView(mView);
    }

    private class WorkerHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MESSAGE_TIMEOUT:
                    cancel();
                    break;
            }
        }
    }
}
