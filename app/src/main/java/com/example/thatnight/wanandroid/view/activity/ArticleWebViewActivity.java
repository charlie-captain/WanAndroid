package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseWebViewActivity;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;

import java.util.regex.Pattern;

/**
 * date: 2018/9/4
 * author: thatnight
 */
public class ArticleWebViewActivity extends BaseWebViewActivity {

    public static final String KEY_RESULT_COLLECTED = "isSelected";
    public static final String KEY_RESULT_POSITION = "position";

    public static final int NO_ORIGINID = 0;

    private int mId, mOriginId, mPosition;
    private boolean isCollect;

    @Override
    protected void initData() {
        super.initData();

        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mPosition = extraIntent.getIntExtra("position", 0);
            mId = extraIntent.getIntExtra("id", 0);
            mOriginId = extraIntent.getIntExtra("originId", 0);
            mTextTitle = extraIntent.getStringExtra("title");
            mLink = extraIntent.getStringExtra("url");
            isCollect = extraIntent.getBooleanExtra("isCollect", false);
        }
    }

    @Override
    protected void initView() {
        super.initView();

        if (!TextUtils.isEmpty(mLink)) {
            mWebView.loadUrl(mLink);
        }
        mActionButton.setSelected(isCollect);

        //过滤html的title
        boolean isHtml = Pattern.matches(".*<em.+?>(.+?)</em>.*", mTextTitle);
        if (isHtml) {
            String newTitle = mTextTitle.replaceAll("<em.+?>", "").replaceAll("</em>", "");
            setTitle(newTitle);
        } else {
            setTitle(mTextTitle);
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
        intent.putExtra(KEY_RESULT_COLLECTED, mActionButton.isSelected());
        intent.putExtra(KEY_RESULT_POSITION, mPosition);
        setResult(RESULT_OK, intent);
    }

    /**
     * 创建实例
     *
     * @param context
     * @param id
     * @param originId
     * @param title
     * @param url
     * @param isCollect
     * @return
     */
    public static Intent newIntent(Context context, int position, int id, int originId, String title, String url, boolean isCollect) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("originId", originId);
        intent.putExtra("url", url);
        intent.putExtra("isCollect", isCollect);
        intent.setClass(context, ArticleWebViewActivity.class);
        return intent;
    }

    /**
     * 创建实例2 不包含originId
     *
     * @param context
     * @param id
     * @param title
     * @param url
     * @param isCollect
     * @return
     */
    public static Intent newIntent(Context context, int position, int id, String title, String url, boolean isCollect) {
        return newIntent(context, position, id, NO_ORIGINID, title, url, isCollect);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_news:
                if (LoginContextUtil.getInstance().getUserState().collect(ArticleWebViewActivity.this)) {
                    UiHelper.setSelected(v);
                    if (mOriginId == NO_ORIGINID) {
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
}
