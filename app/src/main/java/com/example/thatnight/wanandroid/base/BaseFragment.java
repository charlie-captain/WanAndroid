package com.example.thatnight.wanandroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thatnight on 2017.10.27.
 */

public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected View mRootView;
    protected boolean mIsPrepare;
    protected boolean mIsVisible;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            initData(getArguments());
            initView();
            initListener();
            mIsPrepare = true;
            onLazyLoad();
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisibleToUser();
        } else {
            mIsVisible = false;
        }
    }

    private void onVisibleToUser() {
        if (mIsVisible && mIsPrepare) {
            onLazyLoad();
        }
    }

    protected <T extends View> T $(int resId) {
        if (mRootView == null) {
            return null;
        }
        return (T) mRootView.findViewById(resId);
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
        Log.d("onlazy", "onDestroyView: ");
        super.onDestroyView();
        if (mRootView != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }
}
