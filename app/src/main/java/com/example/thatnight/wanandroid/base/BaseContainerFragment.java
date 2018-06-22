package com.example.thatnight.wanandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.view.activity.MainActivity;

/**
 * date: 2018/6/22
 * author: thatnight
 */
public abstract class BaseContainerFragment extends BaseFragment {

    protected Fragment mSupportFragment;

    protected android.app.Fragment mAppFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_container;
    }

    @Override
    protected void initView() {
        getSupportFragment();
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.animator.antor_fade_in, R.animator.antor_fade_out).add(R.id.fl_content, mSupportFragment).commit();
    }

    @Override
    protected void initData(Bundle arguments) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void isLoading(boolean isLoading) {

    }


    public Fragment getSupportFragment() {
        if (mSupportFragment == null) {
            mSupportFragment = initSupportFragment();
        }
        return mSupportFragment;
    }

    protected Fragment initSupportFragment() {
        return null;
    }

}

