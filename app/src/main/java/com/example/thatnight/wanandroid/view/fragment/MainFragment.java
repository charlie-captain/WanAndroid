package com.example.thatnight.wanandroid.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.FragmentAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
    protected BaseModel initModel() {
        return null;
    }

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
        List<String> title = Arrays.asList("最新", "分类");
        mFragments = new ArrayList<>();
        mFragments.add(new NewsFragment());
        mFragments.add(new ClassifyFragment());
        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragments, title);
    }

    @Override
    protected void initView() {
        mToolbar = mRootView.findViewById(R.id.tb);
        mToolbar.setTitle("");
        ((AppCompatActivity) mActivity).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        setDraw(true);
        setTitle("WanAndroid");
        mTabLayout = mRootView.findViewById(R.id.tl_main);
        mAppBarLayout = mRootView.findViewById(R.id.apl_main);
        mVpager = mRootView.findViewById(R.id.vp_main);
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
                if (tab.getPosition() == 0) {
                    EventBus.getDefault().post(Constant.TOP_NEWS);
                } else {
                    EventBus.getDefault().post(Constant.TOP_CLASSIFY);
                }
            }
        });
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

    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_main, menu);
        MenuItem searchItem = menu.findItem(R.id.tb_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivityAnim(SearchActivity.class);
                return false;
            }
        });
    }

    @Subscribe
    public void swithToClassify(KeyValue key) {
        if (Constant.SWITCH_TO_CLASSIFY.equals(key.getKey())) {
            mVpager.setCurrentItem(1);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
