package com.example.thatnight.wanandroid.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.FragmentAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by thatnight on 2017.10.27.
 */

public class MainFragment extends BaseFragment {

    private ViewPager mVpager;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private List<BaseFragment> mFragments;
    private FragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData(Bundle arguments) {
        List<String> title = Arrays.asList("最新", "分类", "常用");
        mFragments = Arrays.asList(new NewsFragment(), new ClassifyFragment(), new ClassifyFragment());
        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragments, title);
    }

    @Override
    protected void initView() {
        mToolbar = $(R.id.tb_main);
        mToolbar.setTitle("WanAndroid");
        mTabLayout = $(R.id.tl_main);
        mAppBarLayout = $(R.id.apl_main);
        mVpager = $(R.id.vp_main);
        mVpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpager);
//        mTabLayout.setTabsFromPagerAdapter(mAdapter);
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
}
