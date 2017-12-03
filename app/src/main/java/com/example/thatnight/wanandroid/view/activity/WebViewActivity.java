package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;
import com.example.thatnight.wanandroid.mvp.contract.WebContract;
import com.example.thatnight.wanandroid.mvp.model.WebModel;
import com.example.thatnight.wanandroid.mvp.presenter.WebPresenter;
import com.example.thatnight.wanandroid.utils.ProgressDialogUtil;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewActivity extends SwipeBackActivity<WebContract.IWebView, WebPresenter> implements View.OnClickListener, WebContract.IWebView {

    private WebView mWebView;
    private String mTitle, mLink;
    private int mId, mOriginId;
    private boolean isCollect;
    private FloatingActionButton mActionButton;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mId = extraIntent.getIntExtra("id", 0);
            mOriginId = extraIntent.getIntExtra("originId", 0);
            mTitle = extraIntent.getStringExtra("title");
            mLink = extraIntent.getStringExtra("url");
            isCollect = extraIntent.getBooleanExtra("isCollect", false);
        }
    }

    @Override
    protected WebModel initModel() {
        return new WebModel();
    }

    @Override
    protected WebPresenter getPresenter() {
        return new WebPresenter();
    }

    @Override
    protected void initView() {
        mShowBack = true;
        mWebView = $(R.id.wb);
        mActionButton = $(R.id.fabtn_news);
        setIbMenu(R.drawable.ic_share);
        setViewData();
    }

    private void setViewData() {
        mWebView.clearCache(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        if (!TextUtils.isEmpty(mLink)) {
            mWebView.loadUrl(mLink);
        }
        mActionButton.setSelected(isCollect);
        setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_OK);
            super.onBackPressed();
        }
    }

    @Override
    protected void initListener() {
        mIbMenu.setOnClickListener(this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                ProgressDialogUtil.show(WebViewActivity.this);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                ProgressDialogUtil.dismiss();
            }
        });
        mActionButton.setOnClickListener(this);
    }

    public static Intent newIntent(Context context,
                                   int id, int originId,
                                   String title, String url, boolean isCollect) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("originId", originId);
        intent.putExtra("url", url);
        intent.putExtra("isCollect", isCollect);
        intent.setClass(context, WebViewActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                ViewUtil.setSelected(v);
                if (mOriginId == 0) {
                    mPresenter.get(v.isSelected(), String.valueOf(mId));
                } else {
                    mPresenter.get(String.valueOf(mId), String.valueOf(mOriginId));
                }
                break;
            case R.id.tb_menu:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "向你分享" + "\"" + mTitle + "\"" + ": \n" + mLink);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            default:
                break;
        }
    }

    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void isSuccess(boolean isSuccess, String s) {
        if (!isSuccess) {
            ViewUtil.setSelected(mActionButton);
        }
        showToast(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
