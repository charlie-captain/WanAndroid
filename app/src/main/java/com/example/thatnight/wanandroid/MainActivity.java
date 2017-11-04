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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.view.fragment.CollectFragment;
import com.example.thatnight.wanandroid.view.fragment.MainFragment;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        mDrawerLayout = findViewById(R.id.dv_main);
        mNavigationView = findViewById(R.id.nv_main);
        mNavigationView.setItemIconTintList(null);
        mName = mNavigationView.getHeaderView(0).findViewById(R.id.tv_nv_header_name);
        mNavigationView.setNavigationItemSelectedListener(this);
        initData();

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
            SharePreferenceUtil.put(this, "account", mAccount.getUsername());
            SharePreferenceUtil.put(this, "password", mAccount.getPassword());
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
