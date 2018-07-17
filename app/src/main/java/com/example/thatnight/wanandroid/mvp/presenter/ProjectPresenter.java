package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectData;
import com.example.thatnight.wanandroid.entity.ProjectItem;
import com.example.thatnight.wanandroid.mvp.contract.BaseFuncContract;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.mvp.model.ProjectModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.view.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectPresenter extends BaseFuncPresenter<ProjectContract.IView> implements ProjectContract.IPresenter {

    protected ProjectModel mProjectModel;

    public ProjectPresenter() {
        mProjectModel = new ProjectModel();
    }

    @Override
    public void getProject(final boolean isRefresh, String id, int page) {
        mProjectModel.getProject(isRefresh, id, page, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                if (view == null) {
                    return;
                }
                view.isLoading(false);
                if (Constant.CODE_SUCCESS == msg.getErrorCode()) {
                    if (isRefresh) {
                        view.refreshData((List<Object>) msg.getData());
                    } else {
                        view.loadmoreData((List<Object>) msg.getData());
                    }
                } else {
                    view.showToast(msg.getErrorMsg().toString());
                }
            }
        });
    }

    @Override
    public void getProjectParent() {
        mProjectModel.getProjectParent(new MvpCallback() {
            @Override
            public void onResult(Msg msg) {

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
        });
    }


}
