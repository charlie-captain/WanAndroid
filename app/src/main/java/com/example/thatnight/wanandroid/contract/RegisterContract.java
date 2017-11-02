package com.example.thatnight.wanandroid.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.bean.Account;
import com.example.thatnight.wanandroid.bean.Msg;

/**
 * Created by thatnight on 2017.11.1.
 */

public class RegisterContract {

    public interface IView extends BaseContract.IBaseView {
        String getName();

        String getPassword();

        void isSuccess(boolean isSuccess, Account dataBean);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void register();

        void getResult(Msg msg);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void register(String name, String pwd, IPresenter iPresenter);
    }
}
