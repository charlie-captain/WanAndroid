package com.example.thatnight.wanandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;

public class AboutActivity extends SwipeBackActivity {


    @Override
    protected Boolean isSetStatusBar() {
        return true;
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
        setTitle("关于");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }
}
