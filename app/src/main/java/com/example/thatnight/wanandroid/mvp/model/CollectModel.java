package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectModel extends BaseModel implements CollectContract.IModel {

    @Override
    public void getArticle(final boolean isRefresh, int page, final CollectContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_ARTICLE_COLLECT + page + "/json", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.getResult(isRefresh, null);
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.getResult(isRefresh, msg);
            }
        });
    }

    @Override
    public void collect(String id, String originId, final CollectContract.IPresenter iPresenter) {
        Map<String, String> map = new HashMap<>();
        map.put("originId", originId);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT_UNCOLLECT + id + "/json", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.collectResult(null);
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.collectResult(msg);
            }

        }, map);

    }
}
