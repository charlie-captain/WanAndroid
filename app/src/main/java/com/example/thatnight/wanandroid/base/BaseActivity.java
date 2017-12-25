package com.example.thatnight.wanandroid.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.MyStatusBarUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import skin.support.app.SkinCompatActivity;

/**
 * Created by thatnight on 2017.10.26.
 */


public abstract class BaseActivity<V extends BaseContract.IBaseView,
        P extends BasePresenter> extends SkinCompatActivity implements BaseContract.IBaseView {

    protected Toolbar mToolbar;
    protected TextView mTitle;
    //    protected ImageButton mIbMenu;
    protected boolean mShowBack;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        setStatusBar(isSetStatusBar());
        mPresenter = getPresenter();
        initPresenter();
        init();
        initData();
        initView();
        initListener();
    }

    protected abstract Boolean isSetStatusBar();

    private void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(initModel(), this);
        }
    }

    protected void setStatusBar(Boolean isSet) {
        if (isSet) {
            StatusBarUtil.setTransparent(this);
        }
    }

    protected abstract BaseModel initModel();

    protected abstract P getPresenter();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.tb);
        mTitle = (TextView) findViewById(R.id.tb_title);
//        mIbMenu = (ImageButton) findViewById(R.id.tb_menu);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (mTitle != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
//        if (mIbMenu != null) {
//            mIbMenu.setVisibility(View.GONE);
//        }
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

    protected void setIbMenu(int res) {
//        if (mIbMenu != null) {
//            mIbMenu.setImageResource(res);
//            mIbMenu.setVisibility(View.VISIBLE);
//        }
    }

    protected <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    public void showToast(String s) {
        ToastUtil.showToast(this, s);
    }

    public void startActivityAnim(Context context, Class activity) {
        startActivity(new Intent(context, activity));
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    public void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    public void startActivityForresultAnim(Intent intent, int code) {
        startActivityForResult(intent, code);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
}
