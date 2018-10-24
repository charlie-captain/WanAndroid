package com.example.thatnight.wanandroid.view.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.FragmentAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主界面
 * Created by thatnight on 2017.10.27.
 */
public class MoreFragment extends BaseMenuFragment {

    private ViewPager mVpager;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private List<BaseFragment> mFragments;
    private FragmentAdapter mAdapter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData(Bundle arguments) {
        EventBus.getDefault().register(this);
        List<String> title = Arrays.asList("开发者头条", "美团", "网易考拉", "Gityuan");
        mFragments = new ArrayList<>();
        mFragments.add(MoreItemFragment.newInstance(Constant.MODE_MORE_TOUTIAO));
        mFragments.add(MoreItemFragment.newInstance(Constant.MODE_MORE_MEITUAN));
        mFragments.add(MoreItemFragment.newInstance(Constant.MODE_MORE_WANGYI));
        mFragments.add(MoreItemFragment.newInstance(Constant.MODE_MORE_GITYUAN));
        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragments, title);
    }

    @Override
    protected void initView() {
        mToolbar = mRootView.findViewById(R.id.tb);
        setDraw(true);
        setTitle("导航");
        mTabLayout = mRootView.findViewById(R.id.tl_main);
        mAppBarLayout = mRootView.findViewById(R.id.apl_main);
        mVpager = mRootView.findViewById(R.id.vp_main);
        mVpager.setOffscreenPageLimit(3);
        mVpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                RecyclerView recyclerView = ((MoreItemFragment) mFragments.get(pos)).getRv();
                if (recyclerView == null) {
                    return;
                }
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int i = linearLayoutManager.findFirstVisibleItemPosition();
                if (i == 0) {
                    EventBus.getDefault().post(Constant.MORE_REFRESH_NEWS);
                } else {
                    EventBus.getDefault().post(Constant.MORE_TOP_NEWS);
                }

            }
        });
    }

    @Override
    protected void initListener() {

    }

    /**
     * 懒加载
     */
    @Override
    protected void onLazyLoad() {


    }

    @Override
    public void isLoading(boolean isLoading) {

    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
