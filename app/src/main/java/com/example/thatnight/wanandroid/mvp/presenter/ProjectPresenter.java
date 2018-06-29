package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectData;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.model.NewsModel;
import com.example.thatnight.wanandroid.mvp.model.ProjectModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.fragment.NewsFragment;
import com.example.thatnight.wanandroid.view.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectPresenter extends BasePresenter<ProjectModel, ProjectFragment> implements ProjectContract.IPresenter {


    @Override
    public void getProject(boolean isRefresh, String id, int page) {
        model.getProject(isRefresh,id,page,this);
    }

    @Override
    public void getProjectParent() {
        model.getProjectParent(this);
    }

    @Override
    public void collect(boolean isCollect, String id) {
        model.collect(isCollect, id, this);
    }

    @Override
    public void getResult(boolean isRefresh, Msg msg) {
        if (view == null) {
            return;
        }
        view.isLoading(false);
        if (msg == null) {
            return;
        }
        if (0 == msg.getErrorCode()) {
            String json = GsonUtil.gsonToJson(msg.getData());
            ProjectData data = GsonUtil.gsonToBean(json, ProjectData.class);
            if (isRefresh) {
                view.refresh(data.getDatas());
            } else {
                view.loadMore(data.getDatas());
            }
        } else {
            view.showToast("网络开小差了," + msg.getErrorMsg());
        }
    }


    @Override
    public void collectResult(boolean isCollect, Msg msg) {
        if (view == null) {
            return;
        }
        if (msg == null) {
            return;
        }

        if (isCollect) {
            if (0 == msg.getErrorCode()) {
                view.isCollectSuccess(true, "收藏成功");
            } else {
                view.isCollectSuccess(false, "收藏失败");
            }
        } else {
            if (0 == msg.getErrorCode()) {
                view.isCollectSuccess(true, "取消收藏成功");
            } else {
                view.isCollectSuccess(false, "取消收藏失败");
            }
        }

    }

    @Override
    public void projectParentResult(Msg msg) {
        if (msg == null) {
            return;
        }

        if (view == null) {
            return;
        }
        if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
            String json = GsonUtil.gsonToJson(msg.getData());
            List<Project> projects = GsonUtil.gsonToList(json, Project.class);
            if (projects == null || projects.size() == 0) {
                return;
            }
            List<KeyValue> parentList = new ArrayList<>();
            for (Project project : projects) {
                KeyValue keyValue = new KeyValue(project.getName(), String.valueOf(project.getId()));
                parentList.add(keyValue);
            }
            view.setProjectParent(parentList);
        } else {


        }

    }


}
