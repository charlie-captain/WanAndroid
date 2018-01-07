package com.example.thatnight.wanandroid.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.thatnight.wanandroid.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class CustomWebView extends WebView {

    protected ProgressBar mProgressBar;

    protected int mPbColor;
    protected int mPbHeight;
    protected int mPbDrawable;

    protected Context mContext;
    protected WindowManager mWindowManager;
    private View mView;

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CustomWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomWebView);
        mPbColor = a.getColor(R.styleable.CustomWebView_color_progressbar, Color.GREEN);
        mPbHeight = a.getDimensionPixelSize(R.styleable.CustomWebView_height_progressbar, 8);
        mPbDrawable = a.getResourceId(R.styleable.CustomWebView_drawable_progressbar, -1);
        a.recycle();

        mContext = context;
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mPbHeight));
        if (mPbDrawable == -1) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.bg_progressbar_web);
            mProgressBar.setProgressDrawable(drawable);
        }
        mProgressBar.setProgress(0);
        addView(mProgressBar);
        init();
    }

    public void setNightMode(Context context) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                , PixelFormat.TRANSLUCENT);
//        lp.gravity = Gravity.TOP;
//        lp.y = mToolbar.getHeight();
        if (mView == null) {
            mView = new View(context);
            mView.setBackgroundColor(context.getResources().getColor(R.color.webviewBackgroundColor_night));
        }
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (mWindowManager != null) {
            mWindowManager.addView(mView, lp);
        }
    }

    private void init() {
        setWebChromeClient(new CustomWebChromeClient());
    }

    public class CustomWebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int i) {
            if (i > 80) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(i);
            }
            super.onProgressChanged(webView, i);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        ViewGroup.LayoutParams lp = mProgressBar.getLayoutParams();
//        lp.width = t;
//        lp.height = l;
//        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mWindowManager != null) {
            if (mView != null) {
                mWindowManager.removeView(mView);
            }
        }
    }
}
