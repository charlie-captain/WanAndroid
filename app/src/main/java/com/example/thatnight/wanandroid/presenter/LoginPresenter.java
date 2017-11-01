package com.example.thatnight.wanandroid.presenter;

import android.util.Log;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.contract.LoginContract;
import com.example.thatnight.wanandroid.model.LoginModel;
import com.example.thatnight.wanandroid.ui.LoginActivity;

/**
 * Created by thatnight on 2017.11.1.
 */

public class LoginPresenter extends BasePresenter<LoginModel, LoginActivity> implements LoginContract.ILoginPresenter {


    @Override
    public void login() {
        model.login(view.getName(), view.getPassword(), this);
    }

    @Override
    public void getResult(Msg msg) {
        view.isLoading(false);
        msg.getErrorCode();
        if (msg == null) {
            view.isSuccess(false, null);
            view.showToast("登陆失败 , 网络出现错误");
        }
        Log.d("login", "login: " + msg.getErrorMsg() + "   " + msg.getErrorCode());

        if (0 == msg.getErrorCode()) {
            view.isSuccess(true, msg.getData());
        } else {
            view.isSuccess(false, null);
            view.showToast("登陆失败 , 用户名或密码错误");
        }
    }
}
