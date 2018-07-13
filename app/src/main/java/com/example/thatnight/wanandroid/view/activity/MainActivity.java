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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.callback.LogoutState;
import com.example.thatnight.wanandroid.callback.OnDrawBtnClickCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.BmobAccount;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.DonateUtil;
import com.example.thatnight.wanandroid.utils.ExitUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.view.fragment.CollectFragment;
import com.example.thatnight.wanandroid.view.fragment.CommentContainerFragment;
import com.example.thatnight.wanandroid.view.fragment.CommentFragment;
import com.example.thatnight.wanandroid.view.fragment.MainFragment;
import com.example.thatnight.wanandroid.view.fragment.ProjectFragment;
import com.example.thatnight.wanandroid.view.fragment.SettingsContainerFragment;
import com.example.thatnight.wanandroid.view.fragment.SettingsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnDrawBtnClickCallback {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mIcon;
    private TextView mName;

    private MainFragment mMainFragment;
    private CollectFragment mCollectFragment;
    private SettingsContainerFragment mSettingsFragment;
    private CommentContainerFragment mCommentFragment;
    private ProjectFragment mProjectFragment;

    private Fragment mLastFragment;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
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
                } else {
                    changeFragmentContent(R.id.nv_menu_settings);
                    mNavigationView.setCheckedItem(R.id.nv_menu_settings);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        initData();
        initFragment();
        showFragment(mMainFragment);
        mNavigationView.setCheckedItem(R.id.nv_menu_main);
        showNewDialog();
    }

    /**
     * 初始化常用类型
     */
    private void initFragment() {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        if (mCollectFragment == null) {
            mCollectFragment = new CollectFragment();
        }
        if (mProjectFragment == null) {
            mProjectFragment = new ProjectFragment();
        }
    }

    //显示更新特性
    private void showNewDialog() {
        boolean isFirst = (boolean) SharePreferenceUtil.getInstance().getBoolean(BuildConfig.VERSION_NAME, true);
        if (isFirst) {
            SharePreferenceUtil.getInstance().putBoolean(BuildConfig.VERSION_NAME, false);
            new AlertDialog.Builder(this).setTitle("更新内容").setMessage(getString(R.string.str_update)).setNegativeButton("知道了", null).show();
        }

    }

    private void initData() {
        mAccount = getIntent().getParcelableExtra("account");

        //如果通过传值登录
        if (mAccount != null) {
            AccountUtil.saveAccount(mAccount);
            SharePreferenceUtil.getInstance().putBoolean(getString(R.string.sp_auto_login), true);
            mName.setText(mAccount.getUsername());
            if (AccountUtil.getBmobAccount() == null) {
                BmobUser.loginByAccount(mAccount.getUsername(), mAccount.getPassword(), new LogInListener<BmobAccount>() {
                    @Override
                    public void done(BmobAccount account, BmobException e) {
                        if (e == null) {
                            updateIcon();
                        } else {
                            //初始化Bmob
                            BmobAccount bmobAccount = new BmobAccount();
                            bmobAccount.setUsername(mAccount.getUsername());
                            bmobAccount.setPassword(mAccount.getPassword());
                            bmobAccount.setId(mAccount.getId());
                            bmobAccount.setNickName(mAccount.getUsername());
                            bmobAccount.signUp(new SaveListener<BmobAccount>() {
                                @Override
                                public void done(final BmobAccount account1, BmobException e) {
                                    if (e == null) {
                                        Log.d("bmob", "done: ");
                                        BmobUser.loginByAccount(mAccount.getUsername(), mAccount.getPassword(), new LogInListener<BmobAccount>() {
                                            @Override
                                            public void done(BmobAccount account, BmobException e) {
                                                if (e == null) {
                                                    updateIcon();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                //拉取最新数据
                BmobAccount.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });
            }
        } else {        //判断是否游客
            boolean isVisitor = SharePreferenceUtil.getInstance().getBoolean("visitor", false);
            if (isVisitor) {
                mName.setText("Visitor");
            } else {    //否则设置已登陆
                Account account = AccountUtil.getAccount();
                if (account != null) {
                    mName.setText(account.getUsername());
                    updateIcon();
                } else {
                    mName.setText("error");
                }
            }
        }
    }

    private void updateIcon() {
        if (AccountUtil.getBmobAccount() != null && AccountUtil.getBmobAccount().getIcon() != null) {
            Glide.with(this).load(AccountUtil.getBmobAccount().getIcon().getUrl()).into(mIcon);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mAccount = intent.getParcelableExtra("account");
        if (mAccount != null) {
            AccountUtil.saveAccount(mAccount);
            SharePreferenceUtil.getInstance().putBoolean(getString(R.string.sp_auto_login), true);
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

    /**
     * 根据pos更改Fragment
     *
     * @param pos
     */
    private void changeFragmentContent(int pos) {
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
            case R.id.nv_menu_project:
                if (mProjectFragment == null) {
                    mProjectFragment = new ProjectFragment();
                }
                showFragment(mProjectFragment);
                break;
            case R.id.nv_menu_user:
                Snackbar.make(mDrawerLayout, "未完待续...", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nv_menu_comment:
                if (mCommentFragment == null) {
                    mCommentFragment = new CommentContainerFragment();
                }
                showFragment(mCommentFragment);
                break;
            case R.id.nv_menu_settings:
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsContainerFragment();
                }
                showFragment(mSettingsFragment);
                break;
            case R.id.nv_menu_donate:
                showDonateDialog();
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
                                startActivityAnim(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        }).setNegativeButton("否", null).show();
                break;
            default:
                break;
        }

    }

    /**
     * 切换 v4. Fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment == mLastFragment) {
            return;
        }
        FragmentTransaction transaction = getV4AppTransaction();
        if (!fragment.isAdded()) {
            if (mLastFragment == null) {
                transaction.add(R.id.fl_content, fragment);
            } else {
                transaction.hide(mLastFragment).add(R.id.fl_content, fragment);
            }
        }
        if (mLastFragment == null) {
            transaction.show(fragment).commit();
        } else {
            transaction.hide(mLastFragment).show(fragment).commit();
        }
        mLastFragment = fragment;
        if (mSettingsFragment != null && mLastFragment == mSettingsFragment && mSettingsFragment.getFragment() != null && ((SettingsFragment) mSettingsFragment.getFragment()).isShowAbout()) {
            mSettingsFragment.getChildFragmentManager().popBackStackImmediate();
            mSettingsFragment.setTitle("设置");
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mSettingsFragment != null && mSettingsFragment.isVisible() && mSettingsFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            mSettingsFragment.getChildFragmentManager().popBackStackImmediate();
            mSettingsFragment.setTitle("设置");
        } else {
            ExitUtil.exitCheck(this, mNavigationView);
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
                        SharePreferenceUtil.getInstance().putString("account", mAccount.getUsername());
                        SharePreferenceUtil.getInstance().putString("password", mAccount.getPassword());
                        mName.setText(mAccount.getUsername());
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (mSettingsFragment != null && mSettingsFragment.getFragment() != null) {
            mSettingsFragment.getFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void switchToClassify(String requestCode) {
        if (Constant.SWITCH_TO_CLASSIFY.equals(requestCode)) {
            mNavigationView.setCheckedItem(R.id.nv_menu_main);
            changeFragmentContent(R.id.nv_menu_main);
        }
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

    public android.app.FragmentTransaction getDefaultAppTransaction() {
        return getFragmentManager().beginTransaction().setCustomAnimations(R.animator.antor_fade_in, R.animator.antor_fade_out);
    }

    public FragmentTransaction getV4AppTransaction() {
        return getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.antor_fade_in, R.animator.antor_fade_out);
    }

    /**
     * 显示捐赠窗口
     */
    private void showDonateDialog() {
        new AlertDialog.Builder(this).setItems(new String[]{"支付宝", "微信"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    DonateUtil.donateAlipay(MainActivity.this);
                } else if (which == 1) {
                    DonateUtil.donateWechat(MainActivity.this);
                }
            }
        }).setNegativeButton("取消", null).show();
    }
}
