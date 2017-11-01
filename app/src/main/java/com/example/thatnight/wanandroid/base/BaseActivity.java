package com.example.thatnight.wanandroid.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thatnight.wanandroid.R;

/**
 * Created by thatnight on 2017.10.26.
 */

public abstract class BaseActivity<V extends BaseContract.IBaseView, P extends BasePresenter> extends AppCompatActivity implements BaseContract.IBaseView {

    protected Toolbar mToolbar;
    protected TextView mTitle;
    protected ImageButton mIbMenu;
    protected boolean mShowBack;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = getPresenter();
        initPresenter();
        init();
        initData();
        initView();
        initListener();
    }

    private void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(initModel(), this);
        }
    }

    protected abstract BaseModel initModel();

    protected abstract P getPresenter();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.tb_title);
        mIbMenu = findViewById(R.id.tb_menu);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (mTitle != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (mIbMenu != null) {
            mIbMenu.setVisibility(View.GONE);
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

    private void setTitle(String title) {
        if (mToolbar != null) {
            mTitle.setText(title);
            setSupportActionBar(mToolbar);
        }
    }

    protected <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
