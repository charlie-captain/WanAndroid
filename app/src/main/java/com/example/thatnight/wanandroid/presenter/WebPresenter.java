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
    public void get(boolean isCollect, String id) {
        view.isLoading(true);
        model.getUrl(isCollect, id, this);
    }

    @Override
    public void getResult(boolean isCollect, Msg msg) {
        if (msg == null) {

        }
        if(isCollect){
            if (0 == msg.getErrorCode()) {
                view.isSuccess(true, "收藏成功");
            } else {
                view.isSuccess(false, "收藏失败");
            }
        }else{
            if (0 == msg.getErrorCode()) {
                view.isSuccess(true, "取消收藏成功");
            } else {
                view.isSuccess(false, "取消收藏失败");
            }
        }
    }




}
