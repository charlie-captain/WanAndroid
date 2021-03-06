package com.example.thatnight.wanandroid.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseContainerFragment;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;

/**
 * 设置容器
 * date: 2018/6/22
 * author: thatnight
 */
public class SettingsContainerFragment extends BaseContainerFragment {


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        setTitle("设置");
        setDraw(true);
    }

    @Override
    protected Fragment initSupportFragment() {
        return new SettingsFragment();
    }

    public  Fragment getFragment(){
        return mSupportFragment;
    }

}
