package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;

/**
 * Created by thatnight on 2017.11.1.
 */

public class LoginContract {
    public interface ILoginView extends BaseContract.IBaseView {
        String getName();

        String getPassword();

        void isSuccess(boolean isSuccess, Account dataBean,String s);
    }

    public interface ILoginPresenter extends BaseContract.IBasePresenter {
        void login();

        void getResult(Msg msg);
    }

    public interface ILoginModel extends BaseContract.IBaseModel {
        void login(String name, String pwd, ILoginPresenter iLoginPresenter);
    }
}
