package com.example.thatnight.wanandroid.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by thatnight on 2017.10.27.
 */

public abstract class BaseFragment<V extends BaseContract.IBaseView,
        P extends BasePresenter> extends Fragment implements BaseContract.IBaseView {

    protected Activity mActivity;
    protected View mRootView;
    protected boolean mIsPrepare;
    protected boolean mIsVisible;
    protected boolean mIsLoad;
    protected Toolbar mToolbar;
    protected P mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = getPresenter();
        initPresenter();
        initToolBar();
        initData(getArguments());
        initView();
        initListener();
        mIsPrepare = true;
        isLoadData();
    }

    protected abstract void isLoadData();

    protected abstract void initToolBar();

    protected void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected abstract P getPresenter();


    protected <T extends View> T $(int resId) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(resId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }


    protected abstract int getLayoutId();

    protected abstract void initView();


    protected abstract void initData(Bundle arguments);

    protected abstract void initListener();

    protected abstract void onLazyLoad();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showToast(String s) {
        ToastUtil.showToast(s);
    }

    public void showSnackBar(String s) {
        if (mRootView != null) {
            Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT);
        }
    }

    public void startActivityAnim(Class activity) {
        startActivity(new Intent(mActivity, activity));
        mActivity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    public void startActivityAnim(Intent activity) {
        startActivity(activity);
        mActivity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    public void startActivityForResultAnim(Intent intent, int code) {
        startActivityForResult(intent, code);
        mActivity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    @Subscribe
    public void onEvent(Msg msg) {

    }
}
