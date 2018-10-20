package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class MoreContract {
    public interface IView extends BaseContract.IBaseView {

        <T> void refreshData(List<T> datas);

        <T> void loadmoreData(List<T> datas);

        void onCollect(boolean isSuccess, String s);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getArticle(boolean isRefresh, String url, int mode, int page);

        void collect(boolean isCollect, Article article);

    }

    public interface IModel extends BaseContract.IBaseModel {
        void getArticle(boolean isRefresh, String url, int mode, int page, MvpBooleanCallback callback);

        void collect(boolean isCollect, Article article, MvpBooleanCallback callback);


    }
}
