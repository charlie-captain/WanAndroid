package com.example.thatnight.wanandroid.mvp.contract;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyContract {
    public interface IView extends BaseFuncView {

        void setExpandPopView(List<KeyValue> parentList, List<List<KeyValue>> parentChildrenList);

        void refreshExpandPopView(List<KeyValue> childrenList);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getParentChildren();

        void getArticle(boolean isRefresh, int page, String value);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getParentChildren(MvpCallback callback);

    }
}
