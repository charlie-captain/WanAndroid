package com.example.thatnight.wanandroid.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.contract.NewsContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class NewsModel extends BaseModel implements NewsContract.IModel {

    @Override
    public void getArticle(final boolean isRefresh, int page, final NewsContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_ARTICLE + page + "/json", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.getResult(isRefresh,null);
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.getResult(isRefresh,msg);
            }
        });
    }

    @Override
    public void collect(final boolean isCollect, String id, final NewsContract.IPresenter iPresenter) {
        if (isCollect) {
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT + id + "/json", new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(byte[] bytes) {
                    String response = new String(bytes);
                    if (TextUtils.isEmpty(response)) {
                        iPresenter.collectResult(isCollect,null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                    iPresenter.collectResult(isCollect,msg);
                }

            }, null);
        } else {
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_UNCOLLECT + id + "/json", new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(byte[] bytes) {
                    String response = new String(bytes);
                    if (TextUtils.isEmpty(response)) {
                        iPresenter.collectResult(isCollect,null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);

                    iPresenter.collectResult(isCollect,msg);
                }

            }, null);
        }

    }


}
