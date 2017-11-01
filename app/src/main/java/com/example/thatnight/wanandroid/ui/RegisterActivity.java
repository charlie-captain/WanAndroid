package com.example.thatnight.wanandroid.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.example.animbutton.AnimButton;
import com.example.thatnight.wanandroid.MainActivity;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.bean.DataBean;
import com.example.thatnight.wanandroid.contract.RegisterContract;
import com.example.thatnight.wanandroid.model.RegisterModel;
import com.example.thatnight.wanandroid.presenter.RegisterPresenter;
import com.example.thatnight.wanandroid.utils.ViewUtil;

public class RegisterActivity extends BaseActivity<RegisterContract.IView, RegisterPresenter> implements RegisterContract.IView {

    private EditText mName, mPwd, mRePwd;
    private AnimButton mBtnRegister;

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
                if (mPwd.getText().toString().equals(mRePwd.getText().toString())) {
                    mBtnRegister.startAnimation();
                    mPresenter.register();
                } else {
                    showToast("两次密码输入不相同");
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
    public void isSuccess(boolean isSuccess, DataBean dataBean) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("account", dataBean);
            startActivity(intent);
            finish();
        } else {
            mBtnRegister.errorAnimation();
        }
    }
}
