package com.example.thatnight.wanandroid.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewArticleRvAdapter;
import com.example.thatnight.wanandroid.adapter.ProjectRvAdapter;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.mvp.contract.WechatContract;
import com.example.thatnight.wanandroid.mvp.presenter.WechatPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.ArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.customview.SkinExpandPopView;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

/**
 * 项目界面
 */
public class WechatFragment extends BaseMenuFragment<WechatContract.IView, WechatPresenter> implements WechatContract.IView, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, ProjectRvAdapter.OnArticleItemClickListener, NewArticleRvAdapter.OnArticleItemClickListener {

    private List<KeyValue> mWechatParents;
    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private SkinExpandPopView mSkinExpandPopView;
    private List<Article> mArticleItems;
    private NewArticleRvAdapter mArticleAdapter;
    private int mPage = 1;
    public static final int REQUEST_CODE = 1;
    private View mIbtnCollect;
    private Article mUnCollectArticle;
    private KeyValue mSelectKey;
    private String mUrl = Constant.URL_BASE + Constant.URL_WECHAT_LIST + "%s/%d/json";

    @Override
    protected WechatPresenter getPresenter() {
        return new WechatPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        setDraw(true);
        setTitle("公众号");
        mSkinExpandPopView = $(R.id.epv_classify);
        mRv = $(R.id.rv_main);
        mRefreshLayout = mRootView.findViewById(R.id.srl_main);
        mArticleAdapter = new NewArticleRvAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mArticleAdapter);
    }

    @Override
    protected void initData(Bundle arguments) {

    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mArticleAdapter.setOnRecyclerViewListener(this);
        mArticleAdapter.setOnIbtnClickListener(this);
        mPage = 1;
    }

    @Override
    protected void onLazyLoad() {
        if (mArticleItems != null && mWechatParents != null) {
            return;
        }
        mPresenter.getWechatParent();
    }


    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void onCollect(boolean isCollect, String result) {
        Snackbar.make(mRootView, result, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiHelper.setSelected(mIbtnCollect);
                }
                if (mUnCollectArticle != null) {
                    mPresenter.collect(true, mUnCollectArticle.getId());
                } else {
                    Snackbar.make(mRootView, "好像什么东西丢了,我忘了.", Snackbar.LENGTH_SHORT);
                }
            }
        }).show();
        if (!isCollect) {
            if (mIbtnCollect != null) {
                UiHelper.setSelected(mIbtnCollect);
            }
        }
    }

    @Override
    public <T> void refreshData(List<T> datas) {
        mArticleItems = (List<Article>) datas;
        mArticleAdapter.updateData(mArticleItems);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public <T> void loadmoreData(List<T> datas) {
        if (mArticleItems == null) {
            mArticleItems = (List<Article>) datas;
        } else {
            mArticleItems.addAll((Collection<? extends Article>) datas);
        }
        mArticleAdapter.updateData(mArticleItems);
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            mRefreshLayout.finishRefresh();
            return;
        }
        mPage = 1;
        mPresenter.getArticle(true, String.format(mUrl, mSelectKey.getValue(), mPage));
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            mRefreshLayout.finishLoadmore();
            return;
        }
        mPage++;
        mPresenter.getArticle(false, String.format(mUrl, mSelectKey.getValue(), mPage));
    }

    @Override
    public void onItemClick(int pos) {
        Article article = mArticleItems.get(pos);
        Intent intent = ArticleWebViewActivity.newIntent(mActivity, article, pos);
        startActivityForResultAnim(intent, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(int pos) {

    }

    @Override
    public void onIbtnClick(View v, int position) {
        if (LoginContextUtil.getInstance().getUserState().collect(getActivity())) {
            mIbtnCollect = v;
            UiHelper.setSelected(v);
            mPresenter.collect(v.isSelected(), mArticleItems.get(position).getId());
            mUnCollectArticle = mArticleItems.get(position);
        }
    }

    @Override
    public void onTypeClick(View v, int position) {

    }

    @Override
    public void onAuthorClick(View v, int position) {
        startActivityAnim(SearchActivity.newIntent(mActivity, mArticleItems.get(position).getAuthor()));
    }

    @Override
    public void onCommentClick(View v, int position) {
        CommentBottomDialogFragment.newInstance(mArticleItems.get(position)).show(getChildFragmentManager(), "comment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data != null) {
                    mArticleItems.get(data.getIntExtra(ArticleWebViewActivity.KEY_RESULT_POSITION, 0)).setCollect(data.getBooleanExtra(ArticleWebViewActivity.KEY_RESULT_COLLECTED, false));
                    mArticleAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void setWechatParent(List<KeyValue> parents) {
        if (mSkinExpandPopView != null) {
            mWechatParents = parents;
            mSkinExpandPopView.setVisibility(View.VISIBLE);
            mSkinExpandPopView.addItemToExpandTab(parents.get(0).getKey(), parents, new OnOneListCallback() {
                @Override
                public void returnKeyValue(int pos, KeyValue keyValue) {
                    mSelectKey = keyValue;
                    onRefresh(mRefreshLayout);
                }
            });
            mSelectKey = parents.get(0);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.tb_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchDialogFragment.newIntent(mSelectKey.getValue())
                        .show(getChildFragmentManager(), "search");
                return false;
            }
        });
    }
}
