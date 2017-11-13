package com.example.thatnight.wanandroid.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewsRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.CollectArticle;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.model.CollectModel;
import com.example.thatnight.wanandroid.mvp.presenter.CollectPresenter;
import com.example.thatnight.wanandroid.utils.HelperCallback;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.example.thatnight.wanandroid.view.activity.WebViewActivity;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.10.27.
 */

public class CollectFragment extends BaseFragment<NewsContract.IView, CollectPresenter>
        implements OnRefreshListener,
        OnLoadmoreListener,
        BaseRecyclerViewAdapter.OnClickRecyclerViewListener,
        NewsRvAdapter.IOnIbtnClickListener,
        View.OnClickListener, CollectContract.IView {

    private List<CollectArticle> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewsRvAdapter mAdapter;
    private int mPage;
    private View mIbtnCollect;
    private Handler mHandler = new Handler();
    private ItemTouchHelper mTouchHelper;

    @Override

    protected void initData(Bundle arguments) {
        mArticles = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mToolbar = mRootView.findViewById(R.id.tb);
        mToolbar.setTitle("");
        mToolbar.setVisibility(View.VISIBLE);
        setTitle("收藏");
        mRv = mRootView.findViewById(R.id.rv_main);
        mRefreshLayout = mRootView.findViewById(R.id.srl_main);
        mAdapter = new NewsRvAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mTouchHelper = new ItemTouchHelper(new HelperCallback(mAdapter));
        mTouchHelper.attachToRecyclerView(mRv);
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
        mPresenter.getArticle(true, mPage);
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
        mPresenter.getArticle(true, 0);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage += 1;
        mPresenter.getArticle(false, mPage);
    }


    @Override
    public void onItemClick(int pos) {
        CollectArticle article = mArticles.get(pos);
        Intent intent = WebViewActivity.newIntent(mActivity,
                article.getId(),
                article.getOriginId(),
                article.getTitle(),
                article.getLink(),
                true);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onItemLongClick(int pos) {

    }


    @Override
    public void onIbtnClick(View v, int position) {
        mIbtnCollect = v;
        ViewUtil.setSelected(v);
        mPresenter.collect(String.valueOf(mArticles.get(position).getId()),
                String.valueOf(mArticles.get(position).getOriginId()));
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
    public void refreshHtml(List<CollectArticle> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        mAdapter.updateData(mArticles);
    }

    @Override
    public void loadMoreHtml(List<CollectArticle> articles) {
        mRefreshLayout.finishLoadmore();
        mArticles.addAll(articles);
        mAdapter.appendData(articles);
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {
        showToast(s);
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                ViewUtil.setSelected(mIbtnCollect);
            }
        } else {
            mPresenter.getArticle(true, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mPresenter.getArticle(true, 0);
//                mRefreshLayout.autoRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
