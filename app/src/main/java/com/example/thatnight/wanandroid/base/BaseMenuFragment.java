package com.example.thatnight.wanandroid.base;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.view.activity.MainActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;

/**
 * Created by thatnight on 2017.10.27.
 */

public abstract class BaseMenuFragment<V extends BaseContract.IBaseView,
        P extends BasePresenter> extends BaseFragment<V, P> implements BaseContract.IBaseView {

    protected TextView mTitle;
    protected ActionBarDrawerToggle mToggle;

    @Override
    protected void isLoadData() {
        onVisibleToUser();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onVisibleToUser();
    }

    protected void onVisibleToUser() {
        if (isHidden()) {
            mIsVisible = false;
        } else {
            mIsVisible = true;
        }
        if (mIsVisible && mIsPrepare && !mIsLoad) {
            onLazyLoad();
            mIsLoad = true;
        }
    }

    @Override
    protected void initToolBar() {
        mToolbar = mRootView.findViewById(R.id.tb);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
    }

    public void setTitle(String title) {
        mTitle = mRootView.findViewById(R.id.tb_title);
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    /**
     * 显示抽屉
     *
     * @param isShow
     */
    protected void setDraw(boolean isShow) {
        if (isShow) {
            final DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();
            //创建Drawer图标
            mToggle = new ActionBarDrawerToggle(mActivity, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_main, menu);
        MenuItem searchItem = menu.findItem(R.id.tb_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivityAnim(SearchActivity.class);
                return false;
            }
        });
    }


}
