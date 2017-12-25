package com.example.thatnight.wanandroid.mvp.presenter;

import android.util.Log;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.ClassifyContract;
import com.example.thatnight.wanandroid.mvp.model.ClassifyModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.fragment.ClassifyFragment;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyPresenter extends BasePresenter<ClassifyModel, ClassifyFragment> implements ClassifyContract.IPresenter {


    @Override
    public void getParentChildren() {
        model.getParentChildren(this);
    }

    @Override
    public void setParentChildren(List<KeyValue> parentList, List<List<KeyValue>> parentChildren) {
        if (view == null) {
            return;
        }
        view.setExpandPopView(parentList, parentChildren);
    }

    @Override
    public void getChildren(String key) {
        model.getChildren(key, this);
    }

    @Override
    public void getChildrenResult(List<KeyValue> childList) {
        if (view == null) {
            return;
        }
        view.refreshExpandPopView(childList);
    }

    @Override
    public void getArticle(boolean isRefresh, int page, String value) {
        model.getArticle(isRefresh, page, value, this);
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
        if (msg == null) {
            view.showSnackBar("网络开小差了");
            return;
        }
        view.isLoading(false);
        if (0 == msg.getErrorCode()) {
//            Log.d("news", "getResult: " + msg.getData());
            String json = GsonUtil.gsonToJson(msg.getData());
            ArticleData data = GsonUtil.gsonToBean(json, ArticleData.class);
            if (isRefresh) {
                view.refreshHtml(data.getDatas());
            } else {
                view.loadMoreHtml(data.getDatas());
            }
        } else {
            view.showSnackBar("网络开小差了,"+msg.getErrorMsg());
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
