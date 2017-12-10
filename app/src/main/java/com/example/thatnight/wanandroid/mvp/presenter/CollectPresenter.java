package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.CollectArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.mvp.model.CollectModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.fragment.CollectFragment;

/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectPresenter extends BasePresenter<CollectModel, CollectFragment> implements CollectContract.IPresenter {

    @Override
    public void getArticle(boolean isRefresh, int page) {
//        view.isLoading(true);
        model.getArticle(isRefresh, page, this);
    }

    @Override
    public void collect(boolean isCollect, String id, String originId) {
        model.collect(isCollect, id, originId, this);
    }


    @Override
    public void getResult(boolean isRefresh, Msg msg) {
        view.isLoading(false);
        if (msg == null) {

        }
        if (0 == msg.getErrorCode()) {
            String json = GsonUtil.gsonToJson(msg.getData());
            CollectArticleData data = GsonUtil.gsonToBean(json, CollectArticleData.class);
            if (isRefresh) {
                view.refreshHtml(data.getDatas());
            } else {
                view.loadMoreHtml(data.getDatas());
            }
        } else {
            view.showToast("网络开小差了");
        }
    }

    @Override
    public void collectResult(boolean isCollect, Msg msg) {
        if (0 == msg.getErrorCode()) {
            if (isCollect) {
                view.isCollectSuccess(true, "收藏成功");
            } else {
                view.isCollectSuccess(true, "取消收藏成功");
            }
        } else {
            if (isCollect) {
                view.isCollectSuccess(false, "收藏失败");
            } else {
                view.isCollectSuccess(false, "取消收藏失败");
            }
        }
    }


}
