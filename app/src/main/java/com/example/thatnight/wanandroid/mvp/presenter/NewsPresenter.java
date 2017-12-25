package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.model.NewsModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.fragment.NewsFragment;

/**
 * Created by thatnight on 2017.11.1.
 */

public class NewsPresenter extends BasePresenter<NewsModel, NewsFragment> implements NewsContract.IPresenter {

    @Override
    public void getArticle(boolean isRefresh, int page) {
//        view.isLoading(true);
        model.getArticle(isRefresh, page, this);
    }

    @Override
    public void collect(boolean isCollect, String id) {
        model.collect(isCollect, id, this);
    }

    @Override
    public void getResult(boolean isRefresh, Msg msg) {
        if (view == null) {
            return;
        }
        view.isLoading(false);
        if (msg == null) {
            return;
        }
        if (0 == msg.getErrorCode()) {
            String json = GsonUtil.gsonToJson(msg.getData());
            ArticleData data = GsonUtil.gsonToBean(json, ArticleData.class);
            if (isRefresh) {
                view.refreshHtml(data.getDatas());
            } else {
                view.loadMoreHtml(data.getDatas());
            }
        } else {
            view.showToast("网络开小差了,"+msg.getErrorMsg());
        }
    }


    @Override
    public void collectResult(boolean isCollect, Msg msg) {
        if (view == null) {
            return;
        }
        if (msg == null) {
            return;
        }

        if (isCollect) {
            if (0 == msg.getErrorCode()) {
                view.isCollectSuccess(true, "收藏成功");
            } else {
                view.isCollectSuccess(false, "收藏失败");
            }
        } else {
            if (0 == msg.getErrorCode()) {
                view.isCollectSuccess(true, "取消收藏成功");
            } else {
                view.isCollectSuccess(false, "取消收藏失败");
            }
        }

    }


}
