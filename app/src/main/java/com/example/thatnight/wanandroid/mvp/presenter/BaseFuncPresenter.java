package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.BaseFuncContract;
import com.example.thatnight.wanandroid.mvp.model.BaseFuncModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * date: 2018/7/16
 * author: thatnightV
 */
public class BaseFuncPresenter<V extends BaseFuncView> extends BasePresenter<V> implements BaseFuncContract.IPresenter {

    protected BaseFuncModel mBaseFuncModel;

    public BaseFuncPresenter() {
        mBaseFuncModel = new BaseFuncModel();
    }

    @Override
    public void collect(final boolean isCollect, int id) {
        mBaseFuncModel.collect(isCollect, id, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                isCollectSuccess(b, msg);
            }
        });
    }

    @Override
    public void getArticle(final boolean isRefresh, final int page) {
        mBaseFuncModel.getArticle(isRefresh, page, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                returnArticle(msg, isRefresh);
            }
        });
    }

    @Override
    public void getArticle(final boolean isRefresh, String url) {
        mBaseFuncModel.getArticle(isRefresh, url, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                returnArticle(msg, isRefresh);
            }
        });
    }

    protected void returnArticle(Msg msg, boolean isRefresh) {
        if (view == null) {
            return;
        }
        view.isLoading(false);
        if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
            if (isRefresh) {
                view.refreshData((List<Article>) msg.getData());
            } else {
                view.loadmoreData((List<Article>) msg.getData());
            }
        } else {
            view.showToast(msg.getErrorMsg().toString());
        }
    }

    protected void isCollectSuccess(boolean isCollect, Msg msg) {

        if (0 == msg.getErrorCode()) {
            if (isCollect) {
                view.onCollect(true, "收藏成功");
            } else {
                view.onCollect(true, "取消收藏成功");
            }
            EventBus.getDefault().post(Constant.REFRESH_COLLECT);
        } else {
            if (isCollect) {
                view.onCollect(false, "收藏失败");
            } else {
                view.onCollect(false, "取消收藏失败");
            }
        }
    }


}
