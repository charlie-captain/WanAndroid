package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.BaseFuncContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.LogUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import okhttp3.Call;


/**
 * date: 2018/7/16
 * author: thatnight
 */
public class BaseFuncModel extends BaseModel implements BaseFuncContract.IModel {

    @Override
    public void getArticle(final boolean isRefresh, int page, final MvpBooleanCallback callback) {
        getArticle(isRefresh, Constant.URL_BASE + Constant.URL_ARTICLE + page + "/json", callback);
    }

    @Override
    public void getArticle(final boolean isRefresh, final String url, final MvpBooleanCallback callback) {
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Msg msg = new Msg();
                msg.setErrorCode(Constant.CODE_ERROR);
                msg.setErrorMsg(e);
                callback.onResult(isRefresh, msg);
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response) || response.startsWith("<html>")) {
                    callback.onResult(isRefresh, DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    return;
                }
                Msg resultMsg = new Msg();
                if (0 == msg.getErrorCode()) {
                    String json = GsonUtil.gsonToJson(msg.getData());
                    ArticleData data = GsonUtil.gsonToBean(json, ArticleData.class);
                    resultMsg.setErrorCode(Constant.CODE_SUCCESS);
                    resultMsg.setData(data.getDatas());
                    callback.onResult(isRefresh, resultMsg);
                } else {
                    resultMsg.setErrorCode(Constant.CODE_ERROR);
                    resultMsg.setErrorMsg("网络开小差了," + msg.getErrorMsg());
                    callback.onResult(isRefresh, resultMsg);
                }
            }
        });
    }

    @Override
    public void collect(final boolean isCollect, int id, final MvpBooleanCallback callback) {
        String url = "";
        if (isCollect) {
            url = Constant.URL_BASE + Constant.URL_COLLECT + id + "/json";
        } else {
            url = Constant.URL_BASE + Constant.URL_UNCOLLECT + id + "/json";
        }

        OkHttpUtil.getInstance().postAsync(url, null, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onResult(isCollect, DataHelper.obtainErrorMsg(e));
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(isCollect, DataHelper.obtainErrorMsg("服务器出错了!"));
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    callback.onResult(isCollect, DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                } else {
                    callback.onResult(isCollect, msg);
                }
            }
        });
    }
}
