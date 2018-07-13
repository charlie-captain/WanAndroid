package com.example.thatnight.wanandroid.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewArticleRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.model.CollectModel;
import com.example.thatnight.wanandroid.mvp.presenter.CollectPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiUtil;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.activity.WebViewActivity;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏页面
 * Created by thatnight on 2017.10.27.
 */

public class CollectFragment extends BaseFragment<NewsContract.IView, CollectPresenter> implements OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, View.OnClickListener, CollectContract.IView, NewArticleRvAdapter.OnArticleItemClickListener {

    private List<Article> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewArticleRvAdapter mAdapter;
    private int mPage;
    private View mIbtnCollect;
    private Handler mHandler = new Handler();
    private ItemTouchHelper mTouchHelper;
    private Article mUnCollectArticle;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void initData(Bundle arguments) {
        EventBus.getDefault().register(this);
        mArticles = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        setDraw(true);
        setTitle("收藏");
        mRv = mRootView.findViewById(R.id.rv_main);
        mRefreshLayout = mRootView.findViewById(R.id.srl_main);
        mAdapter = new NewArticleRvAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mAdapter.setOnRecyclerViewListener(this);
        mAdapter.setOnIbtnClickListener(this);
        mPage = 0;
    }

    @Override
    protected void onLazyLoad() {
        if (mArticles != null && mArticles.size() > 0) {
            return;
        }
        if (LoginContextUtil.getInstance().getUserState().collect(mActivity)) {
            mRefreshLayout.autoRefresh();
        }
    }


    @Override
    protected BaseModel initModel() {
        return new CollectModel();
    }

    @Override
    protected CollectPresenter getPresenter() {
        return new CollectPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        mPresenter.getArticle(true, mPage);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage += 1;
        mPresenter.getArticle(false, mPage);
    }


    @Override
    public void onItemClick(int pos) {
        Article article = mArticles.get(pos);
        Intent intent = WebViewActivity.newIntent(mActivity, article.getId(), article.getOriginId(), article.getTitle(), article.getLink(), true);
        startActivityForResultAnim(intent, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(int pos) {

    }


    @Override
    public void onIbtnClick(View v, int position) {
        mIbtnCollect = v;
        UiUtil.setSelected(v);
        mPresenter.collect(false, String.valueOf(mArticles.get(position).getId()), String.valueOf(mArticles.get(position).getOriginId()));
        mUnCollectArticle = mArticles.get(position);
    }

    @Override
    public void onTypeClick(View v, int position) {
        KeyValue keyValue = new KeyValue(Constant.SWITCH_TO_CLASSIFY, mArticles.get(position).getChapterName());
        EventBus.getDefault().post(Constant.SWITCH_TO_CLASSIFY);
        EventBus.getDefault().post(keyValue);
    }

    @Override
    public void onAuthorClick(View v, int position) {
        startActivityAnim(SearchActivity.newIntent(mActivity, mArticles.get(position).getAuthor()));
    }

    @Override
    public void onCommentClick(View v, int position) {
        CommentBottomDialogFragment
                .newInstance(mArticles.get(position))
                .show(getChildFragmentManager(), "comment");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) {
            mRefreshLayout.autoRefresh();
        } else {
            mRefreshLayout.finishRefresh();
        }
    }


    @Override
    public void refreshHtml(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        mAdapter.updateData(mArticles);
    }

    @Override
    public void loadMoreHtml(List<Article> articles) {
        mRefreshLayout.finishLoadmore();
        mArticles.addAll(articles);
        mAdapter.appendData(articles);
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {
        Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiUtil.setSelected(mIbtnCollect);
                }
                if (mUnCollectArticle != null) {
                    mPresenter.collect(true, String.valueOf(mUnCollectArticle.getId()), String.valueOf(mUnCollectArticle.getOriginId()));
                } else {
                    Snackbar.make(mRootView, "好像什么东西丢了,我忘了.", Snackbar.LENGTH_SHORT);
                }
            }
        }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                UiUtil.setSelected(mIbtnCollect);
            }
        } else {
            mPresenter.getArticle(true, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                mPresenter.getArticle(true, 0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLayout(String requestCode) {
        if (Constant.REFRESH.equals(requestCode)) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
