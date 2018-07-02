package com.example.thatnight.wanandroid.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.ProjectRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.model.ProjectModel;
import com.example.thatnight.wanandroid.mvp.presenter.ProjectPresenter;
import com.example.thatnight.wanandroid.utils.UiUtil;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.activity.WebViewActivity;
import com.example.thatnight.wanandroid.view.customview.SkinExpandPopView;
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

    private int mPage = 1;

    private int mSelectPosition = 0;
    public static final int REQUEST_CODE = 1;
    private View mIbtnCollect;
    private ProjectItem mUnCollectProject;


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
                outRect.set(10, 20, 10, 10);
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
        mPage = 1;
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
            mSelectPosition = 0;
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {
        Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiUtil.setSelected(mIbtnCollect);
                }
                if (mUnCollectProject != null) {
                    mPresenter.collect(true, String.valueOf(mUnCollectProject.getId()));
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
//            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        mPresenter.getProject(true, mProjectsParent.get(mSelectPosition).getValue(), mPage);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        mPresenter.getProject(false, mProjectsParent.get(mSelectPosition).getValue(), mPage);
    }

    @Override
    public void onItemClick(int pos) {
        ProjectItem projectItem = mProjectItems.get(pos);
        Intent intent = WebViewActivity.newIntent(mActivity, projectItem.getId(), projectItem.getTitle(), projectItem.getLink(), projectItem.isCollect());
        startActivityForResultAnim(intent, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(int pos) {

    }

    @Override
    public void onIbtnClick(View v, int position) {
        mIbtnCollect = v;
        UiUtil.setSelected(v);
        mPresenter.collect(v.isSelected(), String.valueOf(mProjectItems.get(position).getId()));
        mUnCollectProject = mProjectItems.get(position);
    }

    @Override
    public void onAuthorClick(View v, int position) {
        startActivityAnim(SearchActivity.newIntent(mActivity, mProjectItems.get(position).getAuthor()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                mRefreshLayout.autoRefresh();
            }
        }
    }
}
