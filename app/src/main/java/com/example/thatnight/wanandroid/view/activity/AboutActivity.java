package com.example.thatnight.wanandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;

import java.util.Calendar;
import java.util.Date;

public class AboutActivity extends SwipeBackActivity {

    private TextView mTvVersion, mTvRight;

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
        mTvRight = (TextView) $(R.id.tv_about_right);
        mTvVersion = (TextView) $(R.id.tv_about_version);


        mTvRight.setText("@2017-"+ Calendar.getInstance().get(Calendar.YEAR));
        mTvVersion.setText("WanAndroid : " + BuildConfig.VERSION_NAME);
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
