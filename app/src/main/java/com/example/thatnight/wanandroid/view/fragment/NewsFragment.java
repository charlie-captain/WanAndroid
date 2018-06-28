package com.example.thatnight.wanandroid.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewArticleRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.model.NewsModel;
import com.example.thatnight.wanandroid.mvp.presenter.NewsPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.ViewUtil;
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
 * 最新文章
 * Created by thatnight on 2017.10.27.
 */

public class NewsFragment extends BaseFragment<NewsContract.IView, NewsPresenter>
        implements OnRefreshListener,
        OnLoadmoreListener,
        BaseRecyclerViewAdapter.OnClickRecyclerViewListener,
        View.OnClickListener, NewsContract.IView, NewArticleRvAdapter.OnArticleItemClickListener {

    private List<Article> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewArticleRvAdapter mAdapter;
    private int mPage;
    private View mIbtnCollect;
    private int mSelectPosition;
    private Handler mHandler = new Handler();

    @Override
    protected void initData(Bundle arguments) {
        EventBus.getDefault().register(this);

        mArticles = new ArrayList<>();
    }

    @Override
    protected void initView() {
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
//        mPresenter.getArticle(true, mPage);
        mRefreshLayout.autoRefresh();
    }


    @Override
    protected BaseModel initModel() {
        return new NewsModel();
    }

    @Override
    protected NewsPresenter getPresenter() {
        return new NewsPresenter();
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
        Intent intent = WebViewActivity.newIntent(mActivity,
                article.getId(),
                article.getOriginId(),
                article.getTitle(),
                article.getLink(),
                article.isCollect());
        startActivityForresultAnim(intent, 1);
    }

    @Override
    public void onItemLongClick(int pos) {

    }


    @Override
    public void onIbtnClick(View v, int position) {
        if (LoginContextUtil.getInstance().getUserState().collect(mActivity)) {
            mIbtnCollect = v;
            mSelectPosition = position;
            ViewUtil.setSelected(v);
            mPresenter.collect(v.isSelected(), String.valueOf(mArticles.get(position).getId()));
        }
    }

    @Override
    public void onTypeClick(View v, int position) {
        KeyValue keyValue = new KeyValue(Constant.SWITCH_TO_CLASSIFY, mArticles.get(position).getChapterName());
        EventBus.getDefault().post(keyValue);

    }

    @Override
    public void onAuthorClick(View v, int position) {
        startActivityAnim(SearchActivity.newIntent(mActivity, mArticles.get(position).getAuthor()));
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
//        showToast(s);
        Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT)
                .setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIbtnCollect != null) {
                            ViewUtil.setSelected(mIbtnCollect);
                        }
                        mPresenter.collect(mIbtnCollect.isSelected(), String.valueOf(mArticles.get(mSelectPosition).getId()));
                    }
                }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                ViewUtil.setSelected(mIbtnCollect);
            }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLayout(String requestCode) {
        if (Constant.REFRESH.equals(requestCode) | Constant.REFRESH_NEWS.equals(requestCode)) {
            mRefreshLayout.autoRefresh();
        } else if (Constant.TOP_NEWS.equals(requestCode)) {
            mRv.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
