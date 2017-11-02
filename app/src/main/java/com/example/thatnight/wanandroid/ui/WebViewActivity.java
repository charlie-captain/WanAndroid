package com.example.thatnight.wanandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.bean.Articles;
import com.example.thatnight.wanandroid.contract.WebContract;
import com.example.thatnight.wanandroid.model.WebModel;
import com.example.thatnight.wanandroid.presenter.WebPresenter;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewActivity extends BaseActivity<WebContract.IWebView, WebPresenter> implements View.OnClickListener, WebContract.IWebView {

    private WebView mWebView;
    private Articles mArticle;
    private FloatingActionButton mActionButton;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mArticle = extraIntent.getParcelableExtra("article");
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
        if (!TextUtils.isEmpty(mArticle.getLink())) {
            mWebView.loadUrl(mArticle.getLink());
        }
        mActionButton = $(R.id.fabtn_news);
        if (mArticle != null) {
            mActionButton.setSelected(mArticle.isCollect());
            setTitle(mArticle.getTitle());
        }
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
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        mActionButton.setOnClickListener(this);
    }

    public static Intent newIntent(Context context, Articles article) {
        Intent intent = new Intent();
        intent.putExtra("article", article);
        intent.setClass(context, WebViewActivity.class);
        return intent;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                ViewUtil.setSelected(v);
                mPresenter.get(v.isSelected(), String.valueOf(mArticle.getId()));
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

}
