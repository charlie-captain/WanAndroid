package com.example.thatnight.wanandroid.mvp.model;


import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import cn.bmob.v3.okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectModel extends BaseModel implements ProjectContract.IModel {


    @Override
    public void getProject(final boolean isRefresh, String id, int page, final ProjectContract.IPresenter iPresenter) {
        String url = Constant.URL_BASE + Constant.URL_PROJECT_LIST + page + "/json?cid=" + id;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iPresenter.getResult(isRefresh, null);
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.getResult(isRefresh, null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.getResult(isRefresh, msg);
            }
        });
    }

    @Override
    public void collect(final boolean isCollect, String id, final ProjectContract.IPresenter iPresenter) {
        String url = "";
        if (isCollect) {
            url = Constant.URL_BASE + Constant.URL_COLLECT + id + "/json";
        } else {
            url = Constant.URL_BASE + Constant.URL_UNCOLLECT + id + "/json";
        }

        OkHttpUtil.getInstance().postAsync(url, null, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.collectResult(isCollect, null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.collectResult(isCollect, msg);
            }
        });
    }

    @Override
    public void getProjectParent(final ProjectContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_PROJECT, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.projectParentResult(null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.projectParentResult(msg);
            }
        });
    }
}
