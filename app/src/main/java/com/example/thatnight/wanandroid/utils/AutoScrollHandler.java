package com.example.thatnight.wanandroid.utils;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * date: 2018/9/4
 * author: thatnight
 * ViewPager轮播
 */
public class AutoScrollHandler extends Handler implements View.OnTouchListener {


    private WeakReference<ViewPager> mViewPager;
    private static final int MSG_SCROLL = 1;
    private static final int MSG_SCROLL_INVISIBLE = 2;
    private long mDelayTime = 3000;
    private boolean isPlay = false;

    public AutoScrollHandler(Looper looper, ViewPager viewPager) {
        super(looper);
        mViewPager = new WeakReference<>(viewPager);
        mViewPager.get().setOnTouchListener(this);
        startScroll();
    }

    public void startScroll() {
        if (isPlay) {
            return;
        }
        removeMessages(MSG_SCROLL);
        sendEmptyMessageDelayed(MSG_SCROLL, mDelayTime);
    }

    public void stopScroll() {
        if (isPlay) {
            removeMessages(MSG_SCROLL);
            isPlay = false;
        }
    }


    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);

        switch (msg.what) {
            case MSG_SCROLL:
                if (mViewPager != null && mViewPager.get() != null) {
                    ViewPager banner = mViewPager.get();
                    int count = banner.getAdapter().getCount();
                    int pos = banner.getCurrentItem();
                    if (pos + 1 < count) {
                        banner.setCurrentItem(pos + 1, true);
                    } else {
                        banner.setCurrentItem(0, true);
                    }
                    sendEmptyMessageDelayed(MSG_SCROLL, mDelayTime);
                    isPlay = true;
                }
                break;
            case MSG_SCROLL_INVISIBLE:

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            startScroll();
        } else {
            stopScroll();
        }
        return false;
    }
}
