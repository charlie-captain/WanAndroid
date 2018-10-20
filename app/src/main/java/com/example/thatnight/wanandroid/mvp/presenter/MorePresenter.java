package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.MoreContract;
import com.example.thatnight.wanandroid.mvp.model.MoreModel;
import com.example.thatnight.wanandroid.view.fragment.MoreItemFragment;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class MorePresenter extends BasePresenter<MoreItemFragment> implements MoreContract.IPresenter {
    private MoreModel mMoreModel;

    public MorePresenter() {
        mMoreModel = new MoreModel();
    }

    @Override
    public void getArticle(final boolean isRefresh, String url, int mode, int page) {
        mMoreModel.getArticle(isRefresh, url, mode, page, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                if (view == null) {
                    return;
                }
                view.isLoading(false);
                if (msg == null) {
                    return;
                }
                if (0 == msg.getErrorCode()) {
                    if (isRefresh) {
                        view.refreshData((List<Article>) msg.getData());
                    } else {
                        view.loadmoreData((List<Article>) msg.getData());
                    }
                } else {
                    view.showToast("网络开小差了," + msg.getErrorMsg());
                }
            }
        });

    }

    @Override
    public void collect(final boolean isCollect, Article article) {
        mMoreModel.collect(isCollect, article, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                if (view == null) {
                    return;
                }
                if (msg == null) {
                    return;
                }
                if (isCollect) {
                    if (0 == msg.getErrorCode()) {
                        view.onCollect(true, "收藏成功");
                    } else {
                        view.onCollect(false, "收藏失败");
                    }
                } else {
                    if (0 == msg.getErrorCode()) {
                        view.onCollect(true, "取消收藏成功");
                    } else {
                        view.onCollect(false, "取消收藏失败");
                    }
                }

            }
        });
    }


}
