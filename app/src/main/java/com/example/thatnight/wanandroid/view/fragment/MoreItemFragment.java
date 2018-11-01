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
import com.example.thatnight.wanandroid.base.BasePagerFragment;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.mvp.contract.MoreContract;
import com.example.thatnight.wanandroid.mvp.presenter.MorePresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.ArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.NormalArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * 导航
 * Created by thatnight on 2017.10.27.
 */

public class MoreItemFragment extends BasePagerFragment<MoreContract.IView, MorePresenter> implements OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, View.OnClickListener, MoreContract.IView, NewArticleRvAdapter.OnArticleItemClickListener {

    private List<Article> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewArticleRvAdapter mAdapter;
    private int mPage;
    private View mIbtnCollect;
    private int mSelectPosition;
    private Handler mHandler = new Handler();
    private int mMode = Constant.MODE_MORE_TOUTIAO;
    private static final String KEY_MODE = "key_mode";
    private String mUrl = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;

    public static MoreItemFragment newInstance(int mode) {
        MoreItemFragment moreItemFragment = new MoreItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MODE, mode);
        moreItemFragment.setArguments(bundle);
        return moreItemFragment;
    }

    @Override
    protected void initData(Bundle arguments) {
        EventBus.getDefault().register(this);
        mArticles = new ArrayList<>();
        mMode = arguments.getInt(KEY_MODE);
        mUrl = getMode();
    }

    /**
     * 根据代号获取地址
     *
     * @return
     */
    private String getMode() {
        String url = "";
        switch (mMode) {
            case Constant.MODE_MORE_TOUTIAO:
                mCalendar = Calendar.getInstance();
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH) + 1;
                refreshPreDate(mCalendar);
                url = Constant.URL_TOUTIAO_PREV + mYear + "-" + mMonth + "-" + mDay;
                break;
            case Constant.MODE_MORE_MEITUAN:
                url = Constant.URL_MEITUAN_TAG;
                break;
            case Constant.MODE_MORE_WANGYI:
                url = Constant.URL_WANGYI;
                break;
            case Constant.MODE_MORE_BUS:
                url = Constant.URL_BUS_BLOG;
                break;
            case Constant.MODE_MORE_GITYUAN:
                url = Constant.URL_GITYUAN;
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * 刷新前一天的时间
     *
     * @param calendar
     */
    private void refreshPreDate(Calendar calendar) {
        mDay = calendar.get(Calendar.DAY_OF_MONTH) - mPage;
        if (mDay <= 0) {
            mMonth--;
            if (mMonth <= 0) {
                mMonth = 12;
                mYear--;
            }
            calendar.clear();
            calendar.set(Calendar.YEAR, mYear);
            calendar.set(Calendar.MONTH, mMonth - 1);
            mDay = calendar.getActualMaximum(Calendar.DATE) + mDay;
        }
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
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected MorePresenter getPresenter() {
        return new MorePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        mUrl = getMode();
        mPresenter.getArticle(true, mUrl, mMode, mPage);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage += 1;
//        mPresenter.getArticle(false, mUrl, mMode, mPage);

        switch (mMode) {
            case Constant.MODE_MORE_TOUTIAO:
                refreshPreDate(mCalendar);
                mUrl = Constant.URL_TOUTIAO_PREV + mYear + "-" + mMonth + "-" + mDay;
                mPresenter.getArticle(false, mUrl, mMode, mPage);
                break;
            case Constant.MODE_MORE_GITYUAN:
                if (mPage == 1) {
                    mPage++;
                }
                mUrl = Constant.URL_GITYUAN + "page" + mPage;
                mPresenter.getArticle(false, mUrl, mMode, mPage);
                break;
            default:
                mRefreshLayout.finishLoadmore(true);
                break;
        }
    }


    @Override
    public void onItemClick(int pos) {
        Article article = mArticles.get(pos);
        Intent intent = NormalArticleWebViewActivity.newIntent(mActivity, pos, article.getTitle(), article.getLink());
        startActivityForResultAnim(intent, 1);
    }

    @Override
    public void onItemLongClick(final int pos) {
        UiHelper.showCopyArticleDialog(mActivity, mArticles, pos);
    }


    @Override
    public void onIbtnClick(View v, int position) {
        if (LoginContextUtil.getInstance().getUserState().collect(mActivity)) {
            mIbtnCollect = v;
            mSelectPosition = position;
            UiHelper.setSelected(v);
            mPresenter.collect(v.isSelected(), mArticles.get(position));
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
    public void onCommentClick(View v, int position) {
        CommentBottomDialogFragment.newInstance(mArticles.get(position)).show(getChildFragmentManager(), "comment");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) {
        } else {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onCollect(boolean isSuccess, String s) {
        Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiHelper.setSelected(mIbtnCollect);
                }
                mPresenter.collect(mIbtnCollect.isSelected(), mArticles.get(mSelectPosition));
            }
        }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                UiHelper.setSelected(mIbtnCollect);
            }
        } else {
            mArticles.get(mSelectPosition).setCollect(mIbtnCollect.isSelected());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void refreshData(List<T> datas) {
        mArticles.clear();
        mArticles.addAll((Collection<? extends Article>) datas);
        mAdapter.updateData(mArticles);
    }

    @Override
    public <T> void loadmoreData(List<T> datas) {
        mRefreshLayout.finishLoadmore();
        mArticles.addAll((Collection<? extends Article>) datas);
        mAdapter.updateData(mArticles);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    mArticles.get(data.getIntExtra(ArticleWebViewActivity.KEY_RESULT_POSITION, 0)).setCollect(data.getBooleanExtra(ArticleWebViewActivity.KEY_RESULT_COLLECTED, false));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String requestCode) {
        switch (requestCode) {
            case Constant.MORE_REFRESH_NEWS:
                mRefreshLayout.autoRefresh();
                break;
            case Constant.MORE_TOP_NEWS:
                mRv.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public RecyclerView getRv() {
        return mRv;
    }
}
