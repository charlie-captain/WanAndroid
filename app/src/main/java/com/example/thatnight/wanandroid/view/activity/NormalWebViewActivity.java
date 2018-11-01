package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseWebViewActivity;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.regex.Pattern;

/**
 * date: 2018/9/4
 * author: thatnight
 */
public class NormalWebViewActivity extends BaseWebViewActivity {

    public static final String KEY_RESULT_POSITION = "position";

    private int mPosition;

    @Override
    protected void initData() {
        super.initData();

        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mPosition = extraIntent.getIntExtra("position", 0);
            mLink = extraIntent.getStringExtra("url");
        }
    }

    @Override
    protected void initView() {
        super.initView();

        mActionButton.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mLink)) {
            mWebView.loadUrl(mLink);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    public void isSuccess(boolean isSuccess, String s) {
        if (!isSuccess) {
            UiHelper.setSelected(mActionButton);
        }
        showToast(s);
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_POSITION, mPosition);
        setResult(RESULT_OK, intent);
    }

    /**
     * 创建实例
     *
     * @param context
     * @param url
     * @return
     */
    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(context, NormalWebViewActivity.class);
        return intent;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                break;
            default:
                break;
        }
    }
}
