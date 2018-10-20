package com.example.thatnight.wanandroid.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.ProjectVpAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.presenter.ProjectPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.ArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.customview.SkinExpandPopView;
import com.example.thatnight.wanandroid.view.customview.ZoomOutPageTranformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 项目界面
 */
public class ProjectVpFragment extends BaseMenuFragment<ProjectContract.IView, ProjectPresenter> implements ProjectContract.IView, ViewPager.OnPageChangeListener, ProjectVpAdapter.OnItemClickListener {

    private List<KeyValue> mProjectsParent;

    private SkinExpandPopView mSkinExpandPopView;

    private List<ProjectItem> mProjectItems;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private int mPage = 1;

    private int mSelectPosition = 0;
    public static final int REQUEST_CODE = 1;
    private View mIbtnCollect;
    private ProjectItem mUnCollectProject;
    private ProjectVpAdapter mAdapter;

    @Override
    protected ProjectPresenter getPresenter() {
        return new ProjectPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project_viewpager;
    }

    @Override
    protected void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        setDraw(true);
        setTitle("项目");
        mSkinExpandPopView = $(R.id.epv_classify);
        mProgressBar = $(R.id.progressbar);
        mViewPager = $(R.id.vp_project);
        mViewPager.setPageMargin(10);
        mViewPager.setPageTransformer(true, new ZoomOutPageTranformer());
        mAdapter = new ProjectVpAdapter(mProjectItems);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle arguments) {
        mProjectItems = new ArrayList<>();
    }

    @Override
    protected void initListener() {
        mAdapter.setOnIbtnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mPage = 1;
    }

    @Override
    protected void onLazyLoad() {
        if (mProjectItems != null && mProjectsParent != null) {
            return;
        }
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
        if (datas == null || datas.size() == 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
        mProjectItems = (List<ProjectItem>) datas;
        refreshAdapter();
    }

    private void refreshAdapter() {
        mAdapter.setProjects(mProjectItems);
        mViewPager.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public <T> void loadmoreData(List<T> datas) {
        if (datas == null || datas.size() == 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (mProjectItems == null) {
            mProjectItems = (List<ProjectItem>) datas;
        } else {
            mProjectItems.addAll((Collection<? extends ProjectItem>) datas);
        }
        refreshAdapter();
        mViewPager.setCurrentItem(mSelectPosition, false);
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
                    onRefresh();
                }
            });
            mSelectPosition = 0;
            onRefresh();
        }
    }

    public void onRefresh() {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            return;
        }
        mPage = 1;
        mPresenter.getProject(true, mSkinExpandPopView.getSelectedKeyValue(0).getValue(), mPage);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onLoadmore() {
        if (mSkinExpandPopView.getSelectedKeyValue(0) == null) {
            return;
        }
        mPage++;
        mPresenter.getProject(false, mSkinExpandPopView.getSelectedKeyValue(0).getValue(), mPage);
        mProgressBar.setVisibility(View.VISIBLE);
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
    public void onItemClick(View v, int pos) {
        ProjectItem projectItem = mProjectItems.get(pos);
        Intent intent = ArticleWebViewActivity.newIntent(mActivity, projectItem, pos);
        startActivityForResultAnim(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data != null) {
                    int pos = data.getIntExtra(ArticleWebViewActivity.KEY_RESULT_POSITION, 0);
                    mProjectItems.get(pos).setCollect(data.getBooleanExtra(ArticleWebViewActivity.KEY_RESULT_COLLECTED, false));
                    refreshAdapter();
                    mViewPager.setCurrentItem(pos);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mProjectItems != null && position == mProjectItems.size() - 1) {
            onLoadmore();
            mSelectPosition = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
