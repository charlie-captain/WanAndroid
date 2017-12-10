package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.callback.OnDrawBtnClickCallback;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.fragment.CollectFragment;
import com.example.thatnight.wanandroid.view.fragment.MainFragment;
import com.example.thatnight.wanandroid.view.fragment.SettingsFragment;

import skin.support.app.SkinCompatActivity;

public class MainActivity extends SkinCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnDrawBtnClickCallback {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mIcon;
    private TextView mName;

    private MainFragment mMainFragment;
    private CollectFragment mCollectFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private Fragment mLastFragment;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dv_main);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main);
        mName = mNavigationView.getHeaderView(0).findViewById(R.id.tv_nv_header_name);
        mNavigationView.setNavigationItemSelectedListener(this);
        initData();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        );
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        mMainFragment = new MainFragment();
        showFragment(mMainFragment);
        mNavigationView.setCheckedItem(R.id.nv_menu_main);
    }

    private void initData() {
        Intent intent = getIntent();
        mAccount = intent.getParcelableExtra("account");
        if (mAccount != null) {
            SharePreferenceUtil.put(getApplicationContext(), "account", mAccount.getUsername());
            SharePreferenceUtil.put(getApplicationContext(), "password", mAccount.getPassword());
            mName.setText(mAccount.getUsername());
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        changeFragmentContent(id);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragmentContent(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case R.id.nv_menu_main:
                if (mMainFragment == null) {
                    mMainFragment = new MainFragment();
                }
                showFragment(mMainFragment);
                break;
            case R.id.nv_menu_collect:
                if (mCollectFragment == null) {
                    mCollectFragment = new CollectFragment();
                }
                showFragment(mCollectFragment);
                break;
            case R.id.nv_menu_user:
                Snackbar.make(mDrawerLayout, "未完待续...", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nv_menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                break;
            case R.id.nv_menu_exit:
                new AlertDialog.Builder(this).
                        setTitle("提示").
                        setMessage("是否注销?").
                        setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.anim_right_in,R.anim.anim_right_out);
                                finish();
                            }
                        }).setPositiveButton("否", null).show();
                break;
            default:
                break;
        }

    }

    private void showFragment(Fragment fragment) {
        if (fragment == mLastFragment) {
            return;
        }
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        if (!fragment.isAdded()) {
            if (mLastFragment == null) {
                mTransaction.add(R.id.fl_content, fragment);
            } else {
                mTransaction.hide(mLastFragment).add(R.id.fl_content, fragment);
            }
        }
        if (mLastFragment == null) {
            mTransaction.show(fragment).commit();
        } else {
            mTransaction.hide(mLastFragment).show(fragment).commit();
        }
        mLastFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
        }
    }

    @Override
    public void onDrawBtnClick() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


}
