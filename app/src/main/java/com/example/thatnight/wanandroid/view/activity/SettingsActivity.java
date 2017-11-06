package com.example.thatnight.wanandroid.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;
import com.example.thatnight.wanandroid.view.fragment.SettingsFragment;

public class SettingsActivity extends SwipeBackActivity {

    private SettingsFragment mSettingsFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected BaseModel initModel() {
        return null;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mShowBack = true;
        setTitle("设置");

        mSettingsFragment = new SettingsFragment();
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mTransaction.add(R.id.fl_settings, mSettingsFragment);
        mTransaction.show(mSettingsFragment).commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }
}
