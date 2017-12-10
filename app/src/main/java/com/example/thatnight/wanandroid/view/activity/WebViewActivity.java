package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;
import com.example.thatnight.wanandroid.mvp.contract.WebContract;
import com.example.thatnight.wanandroid.mvp.model.WebModel;
import com.example.thatnight.wanandroid.mvp.presenter.WebPresenter;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.ProgressDialogUtil;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import skin.support.SkinCompatManager;


public class WebViewActivity extends SwipeBackActivity<WebContract.IWebView, WebPresenter> implements View.OnClickListener, WebContract.IWebView {

    private WebView mWebView;
    private String mTitle, mLink;
    private int mId, mOriginId;
    private boolean isCollect;
    private FloatingActionButton mActionButton;
    private ArrayList<String> mPhotoList;
    private FrameLayout mWebLayout;
    private ProgressBar mPbar;
    private boolean isNight = false;

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
//        mWebView = $(R.id.wb);
        mWebLayout = $(R.id.fl_web);
        mActionButton = $(R.id.fabtn_news);
        mWebView = new WebView(getApplicationContext());

        mPbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 7);
        mPbar.setLayoutParams(params);
        Drawable drawable = getResources().getDrawable(R.drawable.bg_progressbar_web);
        mPbar.setProgressDrawable(drawable);
        mPbar.setProgress(0);


        View view = null;
        if ("night".equals(SkinCompatManager.getInstance().getCurSkinName())) {
            isNight = true;
            view = new View(WebViewActivity.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(getResources().getColor(R.color.webviewBackgroundColor_night));
//            mWebLayout.addView(view);
        }

        mWebView.addView(mPbar);
        mWebLayout.addView(mWebView);

        if (view != null) {
            mWebLayout.addView(view);
        }
//        if (isNight) {
//            mWebLayout.bringChildToFront(mWebView);
//        }
        setIbMenu(R.drawable.ic_share);
        setViewData();
    }

    private void setViewData() {
        mWebView.clearCache(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setAllowFileAccess(true);
        if (!TextUtils.isEmpty(mLink)) {
            mWebView.loadUrl(mLink);
        }
        mActionButton.setSelected(isCollect);
        setTitle(mTitle);

        mWebView.addJavascriptInterface(new JavaScriptCallback(), "imagelistner");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                webView.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(webView, s, bitmap);
                if (isNight) {
                    ProgressDialogUtil.show(WebViewActivity.this);
                }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(webView, s);
                if (isNight) {
                    ProgressDialogUtil.dismiss();
                }
                getImageUrls();
                addImageClickListner();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    mPbar.setVisibility(View.GONE);
                } else {
                    if (View.VISIBLE == mPbar.getVisibility()) {
                        mPbar.setVisibility(View.VISIBLE);
                    }
                    mPbar.setProgress(i);
                }
                Log.d("i", "onProgressChanged:   " + i);
            }
        });
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
        mActionButton.setOnClickListener(this);
    }

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
        //函数的功能是在图片点击的时候调用本地java接口并传递url过去
        if (mWebView != null) {
            mWebView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        imagelistner.openImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebLayout.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        ProgressDialogUtil.dismiss();
    }

    public void getImageUrls() {

        OkHttpUtil.getInstance().getAsync(mLink, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {

            }
        }, null);

    }


    class JavaScriptCallback {
        @JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent();
            if (mPhotoList != null && mPhotoList.contains(img)) {
                intent = PhotoActivity.newIntent(WebViewActivity.this, mPhotoList.indexOf(img), mPhotoList);
            } else {
                intent = PhotoActivity.newIntent(WebViewActivity.this, img);
            }
            startActivityAnim(intent);
            Log.d("src", "openImage: " + img);
        }
    }
}
