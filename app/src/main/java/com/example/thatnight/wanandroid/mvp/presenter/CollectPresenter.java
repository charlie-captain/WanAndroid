package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.mvp.model.CollectModel;

/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectPresenter extends BaseFuncPresenter<CollectContract.IView> implements CollectContract.IPresenter {

    protected CollectModel mCollectModel;

    public CollectPresenter() {
        mCollectModel = new CollectModel();
    }

    @Override
    public void collectParent(boolean isCollect, int id, int originId) {
        mCollectModel.collectParent(isCollect, id, originId, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                isCollectSuccess(b, msg);
            }
        });
    }

    @Override
    public void getCollectArticle(boolean isRefresh, int page) {
        mCollectModel.getArticle(isRefresh, Constant.URL_BASE + Constant.URL_ARTICLE_COLLECT + page + "/json", new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                returnArticle(msg, b);
            }
        });
    }

}
