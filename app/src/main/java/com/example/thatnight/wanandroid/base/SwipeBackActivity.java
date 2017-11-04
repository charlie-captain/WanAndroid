package com.example.thatnight.wanandroid.base;

import android.os.Bundle;
import android.view.View;

import com.example.thatnight.wanandroid.view.customview.swipeback.SwipeBackActivityBase;
import com.example.thatnight.wanandroid.view.customview.swipeback.SwipeBackActivityHelper;
import com.example.thatnight.wanandroid.view.customview.swipeback.SwipeBackLayout;
import com.example.thatnight.wanandroid.view.customview.swipeback.Utils;


/**
 * Created by thatnight on 2017.11.4.
 */

public abstract class SwipeBackActivity<V extends BaseContract.IBaseView, P
        extends BasePresenter>
        extends BaseActivity<V, P>
        implements BaseContract.IBaseView, SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
