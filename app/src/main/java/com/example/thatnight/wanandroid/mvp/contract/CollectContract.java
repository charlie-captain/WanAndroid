package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.CollectArticle;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectContract {
    public interface IView extends BaseContract.IBaseView {

        void refreshHtml(List<CollectArticle> articles);

        void loadMoreHtml(List<CollectArticle> articles);

        void isCollectSuccess(boolean isSuccess, String s);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getArticle(boolean isRefresh, int page);


        void collect(boolean isCollect, String id, String originId);

        void getResult(boolean isRefresh, Msg msg);

        void collectResult(boolean isCollect, Msg msg);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getArticle(boolean isRefresh, int page, IPresenter iPresenter);

        void collect(boolean isCollect, String id, String originId, IPresenter iPresenter);


    }
}
