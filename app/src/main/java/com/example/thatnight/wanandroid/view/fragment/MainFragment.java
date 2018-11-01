package com.example.thatnight.wanandroid.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.BannerAdapter;
import com.example.thatnight.wanandroid.adapter.FragmentAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.constant.EventBusConfig;
import com.example.thatnight.wanandroid.data.MainDataController;
import com.example.thatnight.wanandroid.entity.BannerEntity;
import com.example.thatnight.wanandroid.utils.AutoScrollHandler;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.activity.NormalArticleWebViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主界面
 * Created by thatnight on 2017.10.27.
 */
public class MainFragment extends BaseMenuFragment implements BannerAdapter.OnBannerClickListener, MainDataController.MainDataListener {

    private ViewPager mVpager;
    private ViewPager mTopViewPager;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private List<BaseFragment> mFragments;

    private MainDataController mMainDataController;
    private FragmentAdapter mAdapter;
    private BannerAdapter mBannerAdapter;
    private List<BannerEntity> mBannerEntities;
    private AutoScrollHandler mAutoScrollHandler;
    private boolean mInitBanner = false;

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
        mBannerEntities = new ArrayList<>();
        mBannerAdapter = new BannerAdapter(mActivity, mBannerEntities, this);
    }

    @Override
    protected void initView() {
        mToolbar = mRootView.findViewById(R.id.tb);
        setDraw(true);
        setTitle("首页");
        mTabLayout = mRootView.findViewById(R.id.tl_main);
        mAppBarLayout = mRootView.findViewById(R.id.apl_main);
        mVpager = mRootView.findViewById(R.id.vp_main);
        mVpager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpager);
    }

    @Override
    protected void initListener() {
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
        final float height = getResources().getDimension(R.dimen.main_vp_height);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (!mInitBanner || mAutoScrollHandler == null) {
                    return;
                }
                if (Math.abs(verticalOffset) <= height) {
                    mAutoScrollHandler.startScroll();
                } else {
                    mAutoScrollHandler.stopScroll();
                }
            }


        });

    }

    /**
     * 懒加载
     */
    @Override
    protected void onLazyLoad() {
        mInitBanner = initBanner();
        if (mInitBanner) {
            mMainDataController = new MainDataController(mActivity, this);
        }
    }

    /**
     * 初始化Banner
     * @return
     */
    private boolean initBanner() {
        if (!SharePreferenceUtil.getInstance().optBoolean("first_banner")) {
            SharePreferenceUtil.getInstance().putBoolean("first_banner", true);
            SharePreferenceUtil.getInstance().putBoolean(getString(R.string.pref_banner), true);
        }
        mTopViewPager = mRootView.findViewById(R.id.vp_main_top);

        if (!SharePreferenceUtil.getInstance().optBoolean(getString(R.string.pref_banner))) {
            mTopViewPager.setVisibility(View.GONE);
            return false;
        } else {
            mTopViewPager.setVisibility(View.VISIBLE);
            mTopViewPager.setAdapter(mBannerAdapter);
            mAutoScrollHandler = new AutoScrollHandler(Looper.getMainLooper(), mTopViewPager);
        }

        return true;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }



    @Subscribe
    public void swithToClassify(KeyValue key) {
        if (Constant.SWITCH_TO_CLASSIFY.equals(key.getKey())) {
            mVpager.setCurrentItem(1);
        }
    }

    @Subscribe
    public void onEvent(String requestCode) {
        if (EventBusConfig.OPEN_BANNER.equals(requestCode)) {
            onLazyLoad();
        } else if (EventBusConfig.CLOSE_BANNER.equals(requestCode)) {
            onLazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void clickBanner(View view, int position) {
        if (mBannerEntities != null && mBannerEntities.size() >= position + 1) {
            BannerEntity bannerEntity = mBannerEntities.get(position);
            Intent intent = NormalArticleWebViewActivity.newIntent(mActivity, position, bannerEntity.getTitle(), bannerEntity.getUrl());
            startActivityForResultAnim(intent, 1);
        }
    }

    @Override
    public void updateBanner(List<BannerEntity> entities) {
        mBannerEntities = entities;
        mBannerAdapter.setData(mBannerEntities);
    }
}
