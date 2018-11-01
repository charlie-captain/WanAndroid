package com.example.thatnight.wanandroid.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseMenuFragment;
import com.example.thatnight.wanandroid.base.BasePagerFragment;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.BmobAccount;
import com.example.thatnight.wanandroid.view.activity.NormalWebViewActivity;

import java.util.Calendar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

/**
 * 关于
 */
public class AboutFragment extends BaseMenuFragment {

    private TextView mTvVersion, mTvRight, mTvAbout, mTvCount, mTvGithub;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setTitle("关于");
        mTvRight = (TextView) $(R.id.tv_about_right);
        mTvVersion = (TextView) $(R.id.tv_about_version);
        mTvAbout = (TextView) $(R.id.tv_about_update);
        mTvCount = (TextView) $(R.id.tv_counts);
        mTvGithub = (TextView) $(R.id.tv_github);

        mTvAbout.setText("更新内容：\n" + getString(R.string.str_update));
        mTvRight.setText("@2017-" + Calendar.getInstance().get(Calendar.YEAR));
        mTvVersion.setText("WanAndroid : " + BuildConfig.VERSION_NAME);

        BmobQuery<BmobAccount> query = new BmobQuery<>();
        query.count(BmobAccount.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mTvCount.setText("当前使用人数: " + integer + "人");
                }
            }
        });
    }

    @Override
    protected void initData(Bundle arguments) {

    }

    @Override
    protected void initListener() {
        mTvGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAnim(NormalWebViewActivity.newIntent(mActivity, Constant.URL_GITHUB));
            }
        });
    }

    @Override
    protected void onLazyLoad() {

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }


}
