package com.example.thatnight.wanandroid.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.callback.LogoutState;
import com.example.thatnight.wanandroid.callback.OnDrawBtnClickCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.utils.ExitUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.fragment.CollectFragment;
import com.example.thatnight.wanandroid.view.fragment.MainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnDrawBtnClickCallback {

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
        EventBus.getDefault().register(this);
//        StatusBarUtil.setTransparent(this);
        mFragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dv_main);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main);
        mName = mNavigationView.getHeaderView(0).findViewById(R.id.tv_nv_header_name);
        mIcon = mNavigationView.getHeaderView(0).findViewById(R.id.iv_nv_header_icon);
        mNavigationView.setNavigationItemSelectedListener(this);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginContextUtil.getInstance().getUserState().getClass() == LogoutState.class) {
                    startActivityAnim(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
        initData();

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        );
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        mMainFragment = new MainFragment();
        showFragment(mMainFragment);
        mNavigationView.setCheckedItem(R.id.nv_menu_main);
        showNewDialog();
    }

    //显示更新特性
    private void showNewDialog() {
        boolean isFirst = (boolean) SharePreferenceUtil.get(getApplicationContext(), Constant.UPDATE_DIALOG, true);
        if (isFirst) {
            SharePreferenceUtil.put(getApplicationContext(), Constant.UPDATE_DIALOG, false);
            new AlertDialog.Builder(this)
                    .setTitle("更新内容")
                    .setMessage("修复登录异常\n修复若干bug\n不更新咯...")
                    .setNegativeButton("知道了", null).show();
        }

    }

    private void initData() {
        mAccount = getIntent().getParcelableExtra("account");

        //如果通过传值登录
        if (mAccount != null) {
            SharePreferenceUtil.put(getApplicationContext(), "account", mAccount.getUsername());
            SharePreferenceUtil.put(getApplicationContext(), "password", mAccount.getPassword());
            SharePreferenceUtil.put(getApplicationContext(), getString(R.string.sp_auto_login), true);
            mName.setText(mAccount.getUsername());
        } else {        //判断是否游客
            boolean isVisitor = (boolean) SharePreferenceUtil.get(getApplicationContext(), "visitor", false);
            if (isVisitor) {
                mName.setText("Visitor");
            } else {    //否则设置已登陆
                String userName = (String) SharePreferenceUtil.get(getApplicationContext(), "account", "");
                if (!TextUtils.isEmpty(userName)) {
                    mName.setText(userName);
                } else {
                    mName.setText("error");
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mAccount = intent.getParcelableExtra("account");
        if (mAccount != null) {
            SharePreferenceUtil.put(getApplicationContext(), "account", mAccount.getUsername());
            SharePreferenceUtil.put(getApplicationContext(), "password", mAccount.getPassword());
            SharePreferenceUtil.put(getApplicationContext(), getString(R.string.sp_auto_login), true);
            mName.setText(mAccount.getUsername());
            EventBus.getDefault().post("refresh");
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

            case R.id.nv_menu_comment:
                startActivityAnim(new Intent(this, CommentActivity.class));
                break;
            case R.id.nv_menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                break;
            case R.id.nv_menu_exit:
                new AlertDialog.Builder(this).
                        setTitle("提示").
                        setMessage("是否注销?").
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginContextUtil.getInstance().setUserState(new LogoutState());
                                OkHttpCookieJar.resetCookies();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                onBackPressed();
                            }
                        }).setNegativeButton("否", null).show();
                break;
            default:
                break;
        }

    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Constant.REQUEST_LOGIN == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    mAccount = data.getParcelableExtra("account");
                    if (mAccount != null) {
                        SharePreferenceUtil.put(getApplicationContext(), "account", mAccount.getUsername());
                        SharePreferenceUtil.put(getApplicationContext(), "password", mAccount.getPassword());
                        mName.setText(mAccount.getUsername());
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void switchToClassify(String requestCode) {
        if (Constant.SWITCH_TO_CLASSIFY.equals(requestCode)) {
            mNavigationView.setCheckedItem(R.id.nv_menu_main);
            changeFragmentContent(R.id.nv_menu_main);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            ExitUtil.exitCheck(this, mNavigationView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
