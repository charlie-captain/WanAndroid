package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;
import com.example.thatnight.wanandroid.mvp.contract.WebContract;
import com.example.thatnight.wanandroid.mvp.model.WebModel;
import com.example.thatnight.wanandroid.mvp.presenter.WebPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.ProgressDialogUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import skin.support.SkinCompatManager;


public class WebViewActivity extends SwipeBackActivity<WebContract.IWebView, WebPresenter> implements View.OnClickListener, WebContract.IWebView {

    private WebView mWebView;
    private String mTextTitle, mLink;
    private int mId, mOriginId;
    private boolean isCollect;
    private FloatingActionButton mActionButton;
    private NestedScrollView mNestedScrollView;
    private ArrayList<String> mPhotoList;
    private FrameLayout mWebLayout;
    private ProgressBar mPbar;
    private boolean isNight = false;
    private boolean isFirst = false;

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
            mTextTitle = extraIntent.getStringExtra("title");
            mLink = extraIntent.getStringExtra("url");
            isCollect = extraIntent.getBooleanExtra("isCollect", false);
        }
        isFirst = (boolean) SharePreferenceUtil.get(getApplicationContext(), "webview_update", false);

    }

    @Override
    protected Boolean isSetStatusBar() {
        return true;
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
        mNestedScrollView = $(R.id.nsv_webview);
        mWebLayout = $(R.id.fl_web);
        mActionButton = $(R.id.fabtn_news);
        mWebView = new WebView(getApplicationContext());
        if (!isFirst) {
            Snackbar.make(mWebLayout, "单击标题 , 页面将滚动到顶部哦!", Snackbar.LENGTH_LONG).show();
            SharePreferenceUtil.put(getApplicationContext(), "webview_update", true);
        }

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
        setViewData();
    }

    private void setViewData() {
//        CookieManager cookieManager = CookieManager.getInstance();
//        List<Cookie> cookieList = OkHttpCookieJar.getWanAndroidCookies();
//        StringBuilder sb = new StringBuilder();
//        if (cookieList != null && cookieList.size() > 0) {
//            for (Cookie cookie : cookieList) {
//                sb.append(cookie.toString());
//            }
//        }
//        cookieManager.setCookie(mLink, sb.toString());

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
        setTitle(mTextTitle);

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
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNestedScrollView.scrollTo(0, 0);
                    }
                });
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_web, menu);
        MenuItem shareItem = menu.findItem(R.id.tb_share);
        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "向你分享" + "\"" + mTextTitle + "\"" + ": \n" + mLink);
                startActivity(Intent.createChooser(intent, "分享"));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                if (LoginContextUtil.getInstance().getUserState().collect(WebViewActivity.this)) {
                    ViewUtil.setSelected(v);
                    if (mOriginId == 0) {
                        mPresenter.get(v.isSelected(), String.valueOf(mId));
                    } else {
                        mPresenter.get(String.valueOf(mId), String.valueOf(mOriginId));
                    }
                }
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
            startActivity(intent);
            Log.d("src", "openImage: " + img);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
