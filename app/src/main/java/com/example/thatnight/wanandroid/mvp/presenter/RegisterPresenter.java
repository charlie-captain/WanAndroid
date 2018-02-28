package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.RegisterContract;
import com.example.thatnight.wanandroid.mvp.model.RegisterModel;
import com.example.thatnight.wanandroid.view.activity.RegisterActivity;
import com.example.thatnight.wanandroid.utils.GsonUtil;

/**
 * Created by thatnight on 2017.11.1.
 */

public class RegisterPresenter extends BasePresenter<RegisterModel, RegisterActivity> implements RegisterContract.IPresenter {


    @Override
    public void register() {
        model.register(view.getName(), view.getPassword(), this);

    }

    @Override
    public void getResult(Msg msg) {
        if(view==null){
            return;
        }
        view.isLoading(false);
        if (msg == null) {
            view.isSuccess(false, null, "注册失败 , 服务器开小差了");
            return;
        }
        if (0 == msg.getErrorCode()) {
            String accountJson = GsonUtil.gsonToJson(msg.getData());
            Account account = GsonUtil.gsonToBean(accountJson, Account.class);
            view.isSuccess(true, account, null);
        } else {
            view.isSuccess(false, null, msg.getErrorMsg().toString());
        }
    }
}
