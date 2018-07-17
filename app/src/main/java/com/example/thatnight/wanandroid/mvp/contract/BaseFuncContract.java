package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.entity.Article;

import java.util.List;

/**
 * date: 2018/7/16
 * author: thatnight
 */
public class BaseFuncContract extends BaseContract {

    public interface IView extends BaseFuncView {

    }

    public interface IModel extends IBaseModel {

        void getArticle(boolean isRefresh, int page, MvpBooleanCallback callback);

        void getArticle(String url, boolean isRefresh, MvpBooleanCallback callback);


        void collect(boolean isCollect, int id, MvpBooleanCallback callback);
    }

    public interface IPresenter extends IBasePresenter {

        void collect(boolean isCollect, int id);

        void getArticle(boolean isRefresh, int page);

    }

}
