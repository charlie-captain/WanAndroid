package com.example.thatnight.wanandroid.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.contract.WebContract;
import com.example.thatnight.wanandroid.model.WebModel;
import com.example.thatnight.wanandroid.ui.WebViewActivity;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WebPresenter extends BasePresenter<WebModel, WebViewActivity> implements WebContract.IWebPresenter {



    @Override
    public void get() {

    }

    @Override
    public void getResult(Msg msg) {
        view.isLoading(false);

    }
}
