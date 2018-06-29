package com.example.thatnight.wanandroid.view.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.ProjectRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.model.ProjectModel;
import com.example.thatnight.wanandroid.mvp.presenter.ProjectPresenter;
import com.example.thatnight.wanandroid.view.customview.SkinExpandPopView;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * 项目界面
 */
public class ProjectFragment extends BaseFragment<ProjectContract.IView, ProjectPresenter> implements ProjectContract.IView, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, ProjectRvAdapter.OnArticleItemClickListener {

    private List<KeyValue> mProjectsParent;

    private RecyclerView mRv;

    private RefreshLayout mRefreshLayout;


    private SkinExpandPopView mSkinExpandPopView;

    private List<ProjectItem> mProjectItems;
    private ProjectRvAdapter mProjectRvAdapter;

    private int mPage = 0;

    private int mSelectPosition = 0;


    @Override
    protected BaseModel initModel() {
        return new ProjectModel();
    }

    @Override
    protected ProjectPresenter getPresenter() {
        return new ProjectPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        setDraw(true);
        setTitle("项目");
        mSkinExpandPopView = $(R.id.epv_classify);
        mRv = $(R.id.rv_main);
        mRefreshLayout = mRootView.findViewById(R.id.srl_main);
        mProjectRvAdapter = new ProjectRvAdapter();
        mRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10,10,10,10);
            }
        });
        mRv.setAdapter(mProjectRvAdapter);

    }

    @Override
    protected void initData(Bundle arguments) {

    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mProjectRvAdapter.setOnRecyclerViewListener(this);
        mProjectRvAdapter.setOnIbtnClickListener(this);
        mPage = 0;
    }

    @Override
    protected void onLazyLoad() {
        if (mProjectsParent == null) {
            mPresenter.getProjectParent();
        }
    }


    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void refresh(List<ProjectItem> articles) {
        mProjectItems = articles;
        mProjectRvAdapter.updateData(mProjectItems);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void loadMore(List<ProjectItem> articles) {
        if (mProjectItems == null) {
            mProjectItems = articles;
        } else {
            mProjectItems.addAll(articles);
        }
        mProjectRvAdapter.updateData(mProjectItems);
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void setProjectParent(final List<KeyValue> projectParent) {
        if (mSkinExpandPopView != null) {
            mProjectsParent = projectParent;
            mSkinExpandPopView.setVisibility(View.VISIBLE);
            mSkinExpandPopView.addItemToExpandTab(projectParent.get(0).getKey(), projectParent, new OnOneListCallback() {
                @Override
                public void returnKeyValue(int pos, KeyValue keyValue) {
                    mSelectPosition = pos;
                    mRefreshLayout.autoRefresh();
                }
            });
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        mPresenter.getProject(true, mProjectsParent.get(mSelectPosition).getValue(), mPage);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        mPresenter.getProject(false, mProjectsParent.get(mSelectPosition).getValue(), mPage);
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemLongClick(int pos) {

    }

    @Override
    public void onIbtnClick(View v, int position) {

    }

    @Override
    public void onTypeClick(View v, int position) {

    }

    @Override
    public void onAuthorClick(View v, int position) {

    }
}
