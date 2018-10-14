package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchModel extends BaseFuncModel implements SearchContract.IModel {


    @Override
    public void search(String key, String page, final MvpCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put("k", key);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_SEARCH + page + "/json", map, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onResult(DataHelper.obtainErrorMsg(e.toString()));
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
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
                    callback.onResult(resultMsg);
                } else {
                    resultMsg.setErrorCode(Constant.CODE_ERROR);
                    resultMsg.setErrorMsg("网络开小差了," + msg.getErrorMsg());
                    callback.onResult(resultMsg);
                }
            }
        });
    }

    @Override
    public void getHotKey(final MvpCallback onHotKeyCallback) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_HOT_SEARCH, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                onHotKeyCallback.onResult(DataHelper.obtainErrorMsg(e));
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    onHotKeyCallback.onResult(DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                    return;
                }
                if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    String json = GsonUtil.gsonToJson(msg.getData());
                    List<HotKey> hotKeys = GsonUtil.gsonToList(json, HotKey.class);
                    if (hotKeys != null && hotKeys.size() > 0) {
                        onHotKeyCallback.onResult(DataHelper.obtainMsg(hotKeys));
                    } else {
                        onHotKeyCallback.onResult(DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                    }
                } else {
                    onHotKeyCallback.onResult(msg);
                }
            }
        });
    }
}
