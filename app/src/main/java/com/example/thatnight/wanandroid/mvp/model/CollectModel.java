package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.CollectContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class CollectModel extends BaseFuncModel implements CollectContract.IModel {


    @Override
    public void collectParent(final boolean isCollect, int id, int originId, final MvpBooleanCallback callback) {
        String url = "";
        Map<String, String> map = new HashMap<>();
        if (isCollect) {
            url = Constant.URL_BASE + Constant.URL_COLLECT + originId + "/json";
        } else {
            map.put("originId", String.valueOf(originId));
            url = Constant.URL_BASE + Constant.URL_COLLECT_UNCOLLECT + id + "/json";
        }
        OkHttpUtil.getInstance().postAsync(url, map, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onResult(isCollect, DataHelper.obtainErrorMsg(e));
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(isCollect, null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                callback.onResult(isCollect, msg);
            }

        });
    }

}
