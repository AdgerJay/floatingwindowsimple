package com.forms.floatingwindowsimple.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.forms.floatingwindowsimple.R;
import com.forms.floatingwindowsimple.service.TrafficServiceUtils;


public class FloatView extends LinearLayout {
    private final static String TAG = "FloatView";

    private Context mContext;
    private WindowManager wm;
    private static WindowManager.LayoutParams wmParams;
    public View mContentView;
    private int wmX = 0;
    private int wmY = 0;
    private int mWidth;
    private int mHeight;
    private float mPrevX;
    private float mPrevY;
    private float mStartX;
    private float mStartY;
    private float mLastX;
    private float mLastY;
    private long mLastTime;
    private long mCurrentTime;
    private static boolean mHasShown;


    public FloatView(Context context) {
        super(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;
    }

    public void setLayout(int layout_id) {
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        mContentView = LayoutInflater.from(mContext).inflate(layout_id, null);
        ImageView ivClose = mContentView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                TrafficServiceUtils.stopService(mContext);
            }
        });
        mContentView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPrevX = event.getRawX();
                        mPrevY = event.getRawY();
                        mLastTime = System.currentTimeMillis();
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - mPrevX;
                        float deltaY = event.getRawY() - mPrevY;
                        wmParams.x += deltaX;
                        wmParams.y += deltaY;
                        mPrevX = event.getRawX();
                        mPrevY = event.getRawY();
                        if (wmParams.x < -((mWidth - mContentView.getWidth()) / 2)) {
                            wmParams.x = -((mWidth - mContentView.getWidth()) / 2);
                        }
                        if (wmParams.x > (mWidth - mContentView.getWidth()) / 2)
                            wmParams.x = (mWidth - mContentView.getWidth()) / 2;
                        if (wmParams.y < -((mHeight - mContentView.getHeight()) / 2)) {
                            wmParams.y = -((mHeight - mContentView.getHeight()) / 2);
                        }
                        if (wmParams.y > (mHeight - mContentView.getHeight()) / 2)
                            wmParams.y = (mHeight - mContentView.getHeight()) / 2;
                        try {
                            updateViewPosition();
                        } catch (Exception e) {
                            Log.d(TAG, e.toString());
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        wmX = wmParams.x;
                        wmY = wmParams.y;
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        mCurrentTime = System.currentTimeMillis();
                        if (mCurrentTime - mLastTime < 800) {
                            if (Math.abs(mStartX - mLastX) < 10.0 && Math.abs(mStartY - mLastY) < 10.0) {
                                Toast.makeText(mContext, "悬浮窗被点击", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void updateViewPosition() {
        wm.updateViewLayout(mContentView, wmParams);
    }

    public void create() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                //7.0或以下使用WindowManager.LayoutParams.TYPE_TOAST；
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                //7.1使用WindowManager.LayoutParams.TYPE_PHONE；
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                //8.0或以上使用WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY；
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
            wmParams.gravity = Gravity.CENTER;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.alpha = 1.0f;
            wmParams.x = wmX;
            wmParams.y = wmY;
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 显示自定义悬浮窗口
            wm.addView(mContentView, wmParams);
            mHasShown = true;
        }
    }

    public void show() {
        if (!mHasShown) {
            wm.addView(mContentView, wmParams);
        }
        mHasShown = true;
    }

    public void close() {
        if (mHasShown) {
            wm.removeViewImmediate(mContentView);
        }
        mHasShown = false;
    }
}