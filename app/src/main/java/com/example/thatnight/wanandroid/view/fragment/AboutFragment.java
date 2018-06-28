package com.example.thatnight.wanandroid.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;

import java.util.Calendar;

/**
 * 关于
 */
public class AboutFragment extends BaseFragment {

    private TextView mTvVersion, mTvRight;


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
        setTitle("关于");
        mTvRight = (TextView) $(R.id.tv_about_right);
        mTvVersion = (TextView) $(R.id.tv_about_version);


        mTvRight.setText("@2017-"+ Calendar.getInstance().get(Calendar.YEAR));
        mTvVersion.setText("WanAndroid : " + BuildConfig.VERSION_NAME);
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
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }
}
