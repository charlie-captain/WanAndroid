package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.LoginContract;
import com.example.thatnight.wanandroid.mvp.model.LoginModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

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
        if (msg == null) {
            view.isSuccess(false, null,"登陆失败 , 服务器开小差了");
            return;
        }
        if (0 == msg.getErrorCode()) {
            Account account = GsonUtil.gsonToBean(msg.getData().toString(), Account.class);
            view.isSuccess(true, account,null);
        } else {
            view.isSuccess(false, null,"登陆失败 , "+msg.getErrorMsg());
        }
    }
}
