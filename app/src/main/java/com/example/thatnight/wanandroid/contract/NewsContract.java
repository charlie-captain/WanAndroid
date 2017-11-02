package com.example.thatnight.wanandroid.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.bean.Articles;
import com.example.thatnight.wanandroid.bean.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class NewsContract {
    public interface IView extends BaseContract.IBaseView {

        void refreshHtml(List<Articles> articles);

        void loadMoreHtml(List<Articles> articles);

        void isCollectSuccess(boolean isSuccess, String s);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getArticle(boolean isRefresh, int page);

        void collect(boolean isCollect, String id);

        void getResult(boolean isRefresh, Msg msg);

        void collectResult(boolean isCollect, Msg msg);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getArticle(boolean isRefresh, int page, IPresenter iPresenter);

        void collect(boolean isCollect, String id, IPresenter iPresenter);


    }
}
