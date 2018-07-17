package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.LoginContract;
import com.example.thatnight.wanandroid.mvp.model.LoginModel;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

/**
 * Created by thatnight on 2017.11.1.
 */

public class LoginPresenter extends BasePresenter<LoginActivity> implements LoginContract.ILoginPresenter {

    private LoginModel mLoginModel;

    public LoginPresenter() {
        mLoginModel = new LoginModel();
        
    }

    @Override
    public void login() {
        mLoginModel.login(view.getName(), view.getPassword(), this);
    }

    @Override
    public void register() {
        mLoginModel.register(view.getName(), view.getPassword(), this);
    }

    @Override
    public void getResult(Msg msg) {
        view.isLoading(false);
        if (msg == null) {
            view.isSuccess(false, null, "登陆失败 , 服务器开小差了");
            return;
        }
        if (0 == msg.getErrorCode()) {
            String accountJson = GsonUtil.gsonToJson(msg.getData());
            final Account account = GsonUtil.gsonToBean(accountJson, Account.class);
            if (account == null) {
                view.isSuccess(false, null, "error");
            } else {
                //进行数据保存操作
                AccountUtil.saveAccount(account);
                SharePreferenceUtil.getInstance().putBoolean("visitor", false);
                OkHttpCookieJar.saveCookies();
                LoginContextUtil.getInstance().setUserState(new LoginState());
                view.isSuccess(true, account, null);
            }
        } else {
            view.isSuccess(false, null, msg.getErrorMsg().toString());
        }
    }
}
