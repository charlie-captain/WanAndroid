package com.example.thatnight.wanandroid.mvp.contract;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyContract {
    public interface IView extends BaseContract.IBaseView {

        void setExpandPopView(List<KeyValue> parentList, List<List<KeyValue>> parentChildrenList);

        void refreshExpandPopView(List<KeyValue> childrenList);

        void refreshHtml(List<Article> articles);

        void loadMoreHtml(List<Article> articles);

        void isCollectSuccess(boolean isSuccess, String s);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getParentChildren();

        void setParentChildren(List<KeyValue> parentList, List<List<KeyValue>> parentChildren);

        void getChildren(String key);

        void getChildrenResult(List<KeyValue> childList);

        void getArticle(boolean isRefresh, int page, String value);

        void collect(boolean isCollect, String id);

        void getResult(boolean isRefresh, Msg msg);

        void collectResult(boolean isCollect, Msg msg);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getParentChildren(IPresenter iPresenter);

        void getChildren(String key, IPresenter iPresenter);

        void getArticle(boolean isRefresh, int page, String value, IPresenter iPresenter);

        void collect(boolean isCollect, String id, IPresenter iPresenter);


    }
}
