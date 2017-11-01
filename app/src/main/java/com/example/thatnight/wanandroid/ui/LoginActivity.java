package com.example.thatnight.wanandroid.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.animbutton.AnimButton;
import com.example.thatnight.wanandroid.MainActivity;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.bean.DataBean;
import com.example.thatnight.wanandroid.contract.LoginContract;
import com.example.thatnight.wanandroid.model.LoginModel;
import com.example.thatnight.wanandroid.presenter.LoginPresenter;
import com.example.thatnight.wanandroid.utils.ViewUtil;

public class LoginActivity extends BaseActivity<LoginContract.ILoginView, LoginPresenter> implements LoginContract.ILoginView {

    private EditText mName, mPwd;
    private AnimButton mBtnLogin;
    private Button mRegister;

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected BaseModel initModel() {
        return new LoginModel();
    }

    @Override
    protected void initView() {
        mName = $(R.id.et_login_account);
        mPwd = $(R.id.et_login_psw);
        mRegister = $(R.id.tv_register);
        mBtnLogin = $(R.id.btn_login);
    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.inputSoftWare(false, v);
                mBtnLogin.startAnimation();
                mPresenter.login();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

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
    public void isSuccess(boolean isSuccess, DataBean dataBean) {

        if (isSuccess) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("account", dataBean);
            startActivity(intent);
            finish();
        } else {
            mBtnLogin.errorAnimation();
        }
    }
}
