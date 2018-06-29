package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectModel extends BaseModel implements CollectContract.IModel {

    @Override
    public void getArticle(final boolean isRefresh, int page, final CollectContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_ARTICLE_COLLECT + page + "/json", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                CrashReport.postCatchedException(e);
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
    public void collect(final boolean isCollect, String id, String originId, final CollectContract.IPresenter iPresenter) {
        if (isCollect) {
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT + originId + "/json", null, new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    CrashReport.postCatchedException(e);

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
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("originId", originId);
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT_UNCOLLECT + id + "/json", map, new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    CrashReport.postCatchedException(e);

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
    }

}
