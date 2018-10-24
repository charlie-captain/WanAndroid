package com.example.thatnight.wanandroid.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.ProjectRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.presenter.ProjectPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.ArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.customview.SkinExpandPopView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

/**
 * 项目界面
 */
public class ProjectFragment extends BaseMenuFragment<ProjectContract.IView, ProjectPresenter> implements ProjectContract.IView, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, ProjectRvAdapter.OnArticleItemClickListener {

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
    protected ProjectPresenter getPresenter() {
        return new ProjectPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project;
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
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 20, 20, 10);
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
        mPresenter.getProjectParent();
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
                if (mUnCollectProject != null) {
                    mPresenter.collect(true, mUnCollectProject.getId());
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
        mProjectItems = (List<ProjectItem>) datas;
        mProjectRvAdapter.updateData(mProjectItems);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public <T> void loadmoreData(List<T> datas) {
        if (mProjectItems == null) {
            mProjectItems = (List<ProjectItem>) datas;
        } else {
            mProjectItems.addAll((Collection<? extends ProjectItem>) datas);
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
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            mRefreshLayout.finishRefresh();
            return;
        }
        mPage = 1;
        mPresenter.getProject(true, mSkinExpandPopView.getSelectedKeyValue(0).getValue(), mPage);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            mRefreshLayout.finishLoadmore();
            return;
        }
        mPage++;
        mPresenter.getProject(false, mSkinExpandPopView.getSelectedKeyValue(0).getValue(), mPage);
    }

    @Override
    public void onItemClick(int pos) {
        ProjectItem projectItem = mProjectItems.get(pos);
        Intent intent = ArticleWebViewActivity.newIntent(mActivity, projectItem, pos);
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
            mPresenter.collect(v.isSelected(), mProjectItems.get(position).getId());
            mUnCollectProject = mProjectItems.get(position);
        }
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
                if (data != null) {
                    mProjectItems.get(data.getIntExtra(ArticleWebViewActivity.KEY_RESULT_POSITION, 0)).setCollect(data.getBooleanExtra(ArticleWebViewActivity.KEY_RESULT_COLLECTED, false));
                    mProjectRvAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
