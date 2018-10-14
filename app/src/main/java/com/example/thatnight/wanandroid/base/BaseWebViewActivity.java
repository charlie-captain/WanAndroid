package com.example.thatnight.wanandroid.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.mvp.contract.WebContract;
import com.example.thatnight.wanandroid.mvp.model.WebModel;
import com.example.thatnight.wanandroid.mvp.presenter.WebPresenter;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.ProgressDialogUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.activity.PhotoActivity;
import com.example.thatnight.wanandroid.view.customview.CustomWebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

import okhttp3.Call;
import skin.support.SkinCompatManager;


public abstract class BaseWebViewActivity extends BaseActivity<WebContract.IWebView, WebPresenter> implements View.OnClickListener, WebContract.IWebView, Toolbar.OnMenuItemClickListener {

    protected CustomWebView mWebView;


    protected ArrayList<String> mPhotoList;
    protected FrameLayout mWebLayout;
    protected ProgressBar mPbar;
    protected FloatingActionButton mActionButton;


    protected boolean isNight = false;
    protected boolean isFirst = false;
    protected String mTextTitle, mLink;



    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        mBGASwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        isFirst = (boolean) SharePreferenceUtil.getInstance().getBoolean("webview_update", false);
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
        mWebLayout = $(R.id.fl_web);
        mActionButton = $(R.id.fabtn_news);
        mWebView = new CustomWebView(getApplicationContext());
        mWebLayout.addView(mWebView);
        if ("night".equals(SkinCompatManager.getInstance().getCurSkinName())) {
            isNight = true;
            mWebView.setNightMode(this);
        }
        setViewData();
    }


    /**
     * 设置WebView
     */
    private void setViewData() {

        mWebView.clearCache(true);
        WebSettings settings = mWebView.getSettings();
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setJavaScriptEnabled(true);
        //缓存
        settings.setAppCacheEnabled(true);

        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setAllowFileAccess(true);


        //添加图片监听
        mWebView.addJavascriptInterface(new JavaScriptCallback(), "imagelistner");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                webView.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(webView, s, bitmap);

            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(webView, s);
                getImageUrls();
                addImageClickListner();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });


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
        mActionButton.setOnClickListener(this);

    }

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
        //函数的功能是在图片点击的时候调用本地java接口并传递url过去
        if (mWebView != null) {
            mWebView.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); " + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  " + "        imagelistner.openImage(this.src);  " + "    }  " + "}" + "})()");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_web, menu);
        mToolbar.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "向你分享" + "\"" + mTextTitle + "\"" + ": \n" + mLink);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            case R.id.tb_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("copy_link", mWebView.getUrl()));
                    Snackbar.make(mWebLayout, "复制链接成功", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.tb_browser:
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                startActivity(browser);
                break;
            default:
                break;
        }
        return false;
    }



    @Override
    public void isLoading(boolean isLoading) {

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

        OkHttpUtil.getInstance().getAsync(mLink, null, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {

            }
        });

    }


    class JavaScriptCallback {
        @JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent();
            if (mPhotoList != null && mPhotoList.contains(img)) {
                intent = PhotoActivity.newIntent(BaseWebViewActivity.this, mPhotoList.indexOf(img), mPhotoList);
            } else {
                intent = PhotoActivity.newIntent(BaseWebViewActivity.this, img);
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
