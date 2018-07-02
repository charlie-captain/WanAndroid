package com.example.thatnight.wanandroid.view.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.animbutton.AnimButton;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.callback.LogoutState;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.mvp.contract.LoginContract;
import com.example.thatnight.wanandroid.mvp.model.LoginModel;
import com.example.thatnight.wanandroid.mvp.presenter.LoginPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.UiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginContract.ILoginView, LoginPresenter> implements LoginContract.ILoginView {

    private EditText mName, mPwd, mPwdAgain;
    private AnimButton mBtnLogin;
    private Button mRegister, mVisitor;

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected Boolean isSetStatusBar() {
        return true;
    }

    @Override
    protected BaseModel initModel() {
        return new LoginModel();
    }

    @Override
    protected void initView() {
        mName = $(R.id.et_login_account);
        mPwd = $(R.id.et_login_psw);
        mPwdAgain = $(R.id.et_login_psw_again);
        mRegister = $(R.id.tv_register);
        mBtnLogin = $(R.id.btn_login);
        mVisitor = $(R.id.tv_visitor);

        String userName = SharePreferenceUtil.get(getApplicationContext(), "account", "").toString();
        String password = SharePreferenceUtil.get(getApplicationContext(), "password", "").toString();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            mName.setText(userName);
            mName.setSelection(userName.length());
            mPwd.setText(password);
            mPwd.setSelection(password.length());
            UiUtil.inputSoftWare(false, mPwd);
        }
    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtil.inputSoftWare(false, v);
                if (TextUtils.isEmpty(getName()) || TextUtils.isEmpty(getPassword())) {
                    Snackbar.make(mBtnLogin, "账号或密码不能为空", Snackbar.LENGTH_SHORT).show();
                    mBtnLogin.errorAnimation();
                } else {
                    if (mPwdAgain.getVisibility() == View.GONE) {
                        mPresenter.login();
                    } else {
                        if (TextUtils.isEmpty(mPwdAgain.getText().toString().trim())) {
                            Snackbar.make(mBtnLogin, "请再次输入密码", Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (mPwd.getText().toString().trim().equals(mPwdAgain.getText().toString().trim())) {
                                mPresenter.register();
                            } else {
                                Snackbar.make(mBtnLogin, "两次密码不一样", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPwdAgain.getVisibility() == View.VISIBLE) {
                    mPwdAgain.setVisibility(View.GONE);
                    mPwd.setText("");
                    mPwdAgain.setText("");
                    mName.setText("");
                    mBtnLogin.setStartText("Sign In");
                    mRegister.setText("Sign Up");
                } else {
                    mPwdAgain.setVisibility(View.VISIBLE);
                    mPwd.setText("");
                    mPwdAgain.setText("");
                    mName.setText("");
                    mBtnLogin.setStartText("Sign Up");
                    mRegister.setText("Sign In");
                }
            }
        });
        mVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.put(getApplicationContext(), "visitor", true);
                LoginContextUtil.getInstance().setUserState(new LogoutState());
                startActivityAnim(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public String getName() {
        return mName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPwd.getText().toString().trim();
    }

    @Override
    public void isSuccess(boolean isSuccess, Account dataBean, String s) {
        if (isSuccess) {
            SharePreferenceUtil.put(getApplicationContext(), "visitor", false);
            OkHttpCookieJar.saveCookies(getApplicationContext());
            LoginContextUtil.getInstance().setUserState(new LoginState());
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("account", dataBean);
            startActivityAnim(intent);
            finish();
        } else {
            mBtnLogin.errorAnimation();
            Snackbar.make(mBtnLogin, s, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Subscribe
    public void finishWhenRegister(String result) {
        if ("registerSuccess".equals(result)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
