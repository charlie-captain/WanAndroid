package com.example.thatnight.wanandroid.mvp.contract;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectItem;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectContract {
    public interface IView extends BaseContract.IBaseView, BaseFuncView {

        void setProjectParent(List<KeyValue> projectParent);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getProject(boolean isRefresh, String id, int page);

        void getProjectParent();
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getProject(boolean isRefresh, String id, int page, MvpBooleanCallback callback);

        void getProjectParent(MvpCallback callback);
    }
}
