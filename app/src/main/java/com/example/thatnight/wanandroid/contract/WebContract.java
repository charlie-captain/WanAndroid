package com.example.thatnight.wanandroid.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.bean.DataBean;
import com.example.thatnight.wanandroid.bean.Msg;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WebContract {
    public interface IWebView extends BaseContract.IBaseView {

        void isSuccess(boolean isSuccess, DataBean dataBean);
    }

    public interface IWebPresenter extends BaseContract.IBasePresenter {
        void get();

        void getResult(Msg msg);
    }

    public interface IWebModel extends BaseContract.IBaseModel {
        void getUrl(IWebPresenter iLoginPresenter);
    }
}
