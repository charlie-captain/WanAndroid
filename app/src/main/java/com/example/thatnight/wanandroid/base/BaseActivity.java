package com.example.thatnight.wanandroid.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import skin.support.app.SkinCompatActivity;

/**
 * Created by thatnight on 2017.10.26.
 */


public abstract class BaseActivity<V extends BaseContract.IBaseView, P extends BasePresenter> extends SkinCompatActivity implements BaseContract.IBaseView, BGASwipeBackHelper.Delegate {

    protected Toolbar mToolbar;
    protected TextView mTitle;
    protected boolean mShowBack;
    protected P mPresenter;

    protected BGASwipeBackHelper mBGASwipeBackHelper;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackHelper();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBar(isSetStatusBar());
        mPresenter = getPresenter();
        initPresenter();
        init();
        initData();
        initView();
        initListener();
    }

    protected void initSwipeBackHelper() {
        mBGASwipeBackHelper = new BGASwipeBackHelper(this, this);
        mBGASwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
    }

    protected abstract Boolean isSetStatusBar();

    private void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected void setStatusBar(Boolean isSet) {
        if (isSet) {
            //            mImmersionBar = ImmersionBar.with(this);
            //            mImmersionBar.init();
        }
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mBGASwipeBackHelper.swipeBackward();
    }

    protected abstract BaseModel initModel();

    protected abstract P getPresenter();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.tb);
        mTitle = (TextView) findViewById(R.id.tb_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (mTitle != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mShowBack = false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mToolbar != null && isShowBack()) {
            showBack();
        }
    }

    private void showBack() {
        if (mToolbar == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        if (mBGASwipeBackHelper != null) {
            mBGASwipeBackHelper = null;
        }
    }


    public abstract int getLayoutId();

    /**
     * show back icon
     *
     * @return
     */
    public boolean isShowBack() {
        return mShowBack;
    }

    protected void setTitle(String title) {
        if (mToolbar != null) {
            mTitle.setText(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    public void showToast(String s) {
        ToastUtil.showToast(s);
    }


    /**
     * 带启动动画
     *
     * @param intent
     */
    public void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * @param intent
     * @param code
     */
    public void startActivityForResultAnim(Intent intent, int code) {
        startActivityForResult(intent, code);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    @Override
    public void onBackPressed() {
        if (mBGASwipeBackHelper.isSliding()) {
            return;
        }
        mBGASwipeBackHelper.swipeBackward();

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

}
