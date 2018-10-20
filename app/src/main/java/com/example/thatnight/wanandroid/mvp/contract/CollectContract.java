package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.CollectArticle;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectContract {
    public interface IView extends BaseContract.IBaseView, BaseFuncView {

    }

    public interface IPresenter extends BaseContract.IBasePresenter {

        void collectParent(boolean isCollect, int id, int originId);

        void getCollectArticle(boolean isRefresh, int page);

    }

    public interface IModel extends BaseContract.IBaseModel {

        void collectParent(boolean isCollect, int id, int originId, MvpBooleanCallback callback);
    }
}
