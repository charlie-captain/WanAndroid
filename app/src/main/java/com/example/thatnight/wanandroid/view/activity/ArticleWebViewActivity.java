package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseWebViewActivity;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectItem;
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
    public static final String KEY_ARTICLE = "key_article";
    public static final String KEY_PROJECT = "key_project";
    private Article mArticle;
    private ProjectItem mProjectItem;

    @Override
    protected void initData() {
        super.initData();

        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            mArticle = extraIntent.getParcelableExtra(KEY_ARTICLE);
            mProjectItem = extraIntent.getParcelableExtra(KEY_PROJECT);
            mPosition = extraIntent.getIntExtra("position", 0);
            if (mArticle != null) {
                mId = mArticle.getId() <= -1 ? 0 : mArticle.getId();
                mOriginId = mArticle.getOriginId() <= -1 ? 0 : mArticle.getOriginId();
                mTextTitle = mArticle.getTitle() == null ? "" : mArticle.getTitle();
                mLink = mArticle.getLink() == null ? "" : mArticle.getLink();
                isCollect = mArticle.isCollect() || mArticle.getOriginId() > 0;
            } else if (mProjectItem != null) {
                mId = mProjectItem.getId();
                mTextTitle = mProjectItem.getTitle();
                mLink = mProjectItem.getLink();
                isCollect = mProjectItem.isCollect();
            }
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
     * @param context
     * @param article
     * @param position
     * @return
     */
    public static Intent newIntent(Context context, Article article, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARTICLE, article);
        bundle.putInt("position", position);
        return newIntent(context, bundle);
    }

    public static Intent newIntent(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, ArticleWebViewActivity.class);
        return intent;
    }

    /**
     * 项目
     *
     * @param context
     * @param position
     * @return
     */
    public static Intent newIntent(Context context, ProjectItem project, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PROJECT, project);
        bundle.putInt("position", position);
        return newIntent(context, bundle);
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
