package com.example.thatnight.wanandroid.mvp.model;


import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectData;
import com.example.thatnight.wanandroid.mvp.contract.ProjectContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class ProjectModel extends BaseFuncModel implements ProjectContract.IModel {

    @Override
    public void getProject(final boolean isRefresh, String id, int page, final MvpBooleanCallback callback) {
        String url = Constant.URL_BASE + Constant.URL_PROJECT_LIST + page + "/json?cid=" + id;
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onResult(isRefresh, null);
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(isRefresh, null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    callback.onResult(isRefresh, DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                } else if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    String json = GsonUtil.gsonToJson(msg.getData());
                    ProjectData data = GsonUtil.gsonToBean(json, ProjectData.class);
                    callback.onResult(isRefresh, DataHelper.obtainMsg(data.getDatas()));
                } else {
                    callback.onResult(isRefresh, msg);

                }

            }
        });
    }

    @Override
    public void getProjectParent(final MvpCallback callback) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_PROJECT, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                callback.onResult(msg);
            }
        });
    }
}
