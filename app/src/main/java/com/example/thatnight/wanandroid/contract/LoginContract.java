package com.example.thatnight.wanandroid.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.bean.Account;
import com.example.thatnight.wanandroid.bean.Msg;

/**
 * Created by thatnight on 2017.11.1.
 */

public class LoginContract {
    public interface ILoginView extends BaseContract.IBaseView {
        String getName();

        String getPassword();

        void isSuccess(boolean isSuccess, Account dataBean);
    }

    public interface ILoginPresenter extends BaseContract.IBasePresenter {
        void login();

        void getResult(Msg msg);
    }

    public interface ILoginModel extends BaseContract.IBaseModel {
        void login(String name, String pwd, ILoginPresenter iLoginPresenter);
    }
}
