package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchContract {
    public interface IPresenter extends BaseContract.IBasePresenter {
        void search(boolean isRefresh, String key, String page);

        void collect(boolean isCollect, String id);

    }

    public interface IView extends BaseContract.IBaseView {
        void showArticles(boolean isRefresh, List<Article> articles);

        void isCollectSuccess(boolean isSuccess, String s);

        void error(String s);
    }

    public interface IModel extends BaseContract.IBaseModel {

        interface OnSearchCallback {
            void getResult(List<Article> articles);

            void error(String s);
        }

        interface OnCollectCallback {
            void collectResult(boolean isCollect, String error);
        }

        void collect(boolean isCollect, String id, OnCollectCallback onCollectCallback);

        void search(String key, String page, OnSearchCallback onSearchCallback);
    }

}
