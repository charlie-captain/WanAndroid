package com.example.thatnight.wanandroid.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;

/**
 * Created by thatnight on 2017.10.26.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageButton mIbMenu;
    private boolean mShowBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mToolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.tb_title);
        mIbMenu = findViewById(R.id.tb_menu);
        init();
        initData();
        initListener();
    }

    protected abstract void initListener();

    protected abstract void initData();

    private void init() {
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

    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }
}
