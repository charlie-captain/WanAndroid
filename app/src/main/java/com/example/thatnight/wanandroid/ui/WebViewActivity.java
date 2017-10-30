package com.example.thatnight.wanandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private String mUrl;
    private FloatingActionButton mActionButton;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mUrl = extraIntent.getStringExtra("url");
        }

    }

    @Override
    protected void initView() {
        mShowBack = true;
        mWebView = $(R.id.wb);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
        mActionButton = $(R.id.fabtn_news);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initListener() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        mActionButton.setOnClickListener(this);
    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(context, WebViewActivity.class);
        return intent;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                ViewUtil.setSelected(v);
                break;
            default:
                break;
        }
    }
}
