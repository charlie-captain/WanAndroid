package com.example.thatnight.wanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.thatnight.wanandroid.bean.DataBean;
import com.example.thatnight.wanandroid.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private MainFragment mMainFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private Fragment mLastFragment;
    private DataBean mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        mFragmentManager = getSupportFragmentManager();
        mDrawerLayout = findViewById(R.id.dv_main);
        mNavigationView = findViewById(R.id.nv_main);
        mNavigationView.setNavigationItemSelectedListener(this);


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        );
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        mMainFragment = new MainFragment();
        mLastFragment = mMainFragment;
        showFragment(mMainFragment);
        mNavigationView.setCheckedItem(R.id.nv_menu_main);
    }

    private void initData() {
        Intent intent = getIntent();
        mAccount = intent.getParcelableExtra("account");
        if (mAccount != null) {

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
            case R.id.nv_menu_subscribe:
                break;
            case R.id.nv_menu_settings:
                break;
            case R.id.nv_menu_exit:
                finish();
                break;
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        mTransaction = mFragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            mTransaction.hide(mLastFragment).add(R.id.fl_content, fragment);
        }
        mTransaction.hide(mLastFragment).show(fragment).commit();
        mLastFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
