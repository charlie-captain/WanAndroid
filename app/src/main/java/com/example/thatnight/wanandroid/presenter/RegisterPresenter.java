package com.example.thatnight.wanandroid.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.contract.RegisterContract;
import com.example.thatnight.wanandroid.model.RegisterModel;
import com.example.thatnight.wanandroid.ui.RegisterActivity;

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
        view.isLoading(false);
        msg.getErrorCode();
        if (msg == null) {
            view.isSuccess(false, null);
            view.showToast("注册失败 , 网络出现错误");
        }
        if (0 == msg.getErrorCode()) {
            view.isSuccess(true, msg.getData());
        } else {
            view.isSuccess(false, null);
            view.showToast(msg.getErrorMsg().toString());
        }
    }
}
