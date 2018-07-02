package com.example.thatnight.wanandroid.mvp.contract;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectItem;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectContract {
    public interface IView extends BaseContract.IBaseView {

        void refresh(List<ProjectItem> articles);

        void loadMore(List<ProjectItem> articles);

        void setProjectParent(List<KeyValue> projectParent);

        void isCollectSuccess(boolean isSuccess, String s);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getProject(boolean isRefresh, String id, int page);

        void getProjectParent();

        void collect(boolean isCollect, String id);

        void getResult(boolean isRefresh, Msg msg);

        void collectResult(boolean isCollect, Msg msg);

        void projectParentResult(Msg msg);
    }

    public interface IModel extends BaseContract.IBaseModel {
        void getProject(boolean isRefresh, String id, int page, IPresenter iPresenter);

        void collect(boolean isCollect, String id, IPresenter iPresenter);

        void getProjectParent(IPresenter iPresenter);

    }
}
