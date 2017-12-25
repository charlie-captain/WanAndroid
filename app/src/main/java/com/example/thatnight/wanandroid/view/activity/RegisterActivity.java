package com.example.thatnight.wanandroid.view.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.animbutton.AnimButton;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.SwipeBackActivity;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.mvp.contract.RegisterContract;
import com.example.thatnight.wanandroid.mvp.model.RegisterModel;
import com.example.thatnight.wanandroid.mvp.presenter.RegisterPresenter;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

public class RegisterActivity extends SwipeBackActivity<RegisterContract.IView, RegisterPresenter> implements RegisterContract.IView {

    private EditText mName, mPwd, mRePwd;
    private AnimButton mBtnRegister;

    @Override
    protected Boolean isSetStatusBar() {
        return true;
    }

    @Override
    protected BaseModel initModel() {
        return new RegisterModel();
    }

    @Override
    protected RegisterPresenter getPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void initView() {
        mName = $(R.id.et_register_account);
        mPwd = $(R.id.et_register_pwd);
        mRePwd = $(R.id.et_register_repwd);
        mBtnRegister = $(R.id.btn_register);
    }

    @Override
    protected void initListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.inputSoftWare(false, v);
                if (TextUtils.isEmpty(getName()) || TextUtils.isEmpty(getPassword())
                        || TextUtils.isEmpty(mPwd.getText().toString().trim())) {
                    Snackbar.make(mBtnRegister, "账号或密码不能为空", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (mPwd.getText().toString().trim().equals(mRePwd.getText().toString().trim())) {
                        mBtnRegister.startAnimation();
                        mPresenter.register();
                    } else {
                        Snackbar.make(mBtnRegister, "两次密码输入不相同", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
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
            LoginContextUtil.getInstance().setUserState(new LoginState());
            OkHttpCookieJar.saveCookies(getApplicationContext());
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("account", dataBean);
            EventBus.getDefault().post("registerSuccess");
            startActivityAnim(intent);
            finish();
        } else {
            mBtnRegister.errorAnimation();
            Snackbar.make(mBtnRegister, s, Snackbar.LENGTH_SHORT).show();
        }
    }

}
