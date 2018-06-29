package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.okhttp3.Call;


/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchModel extends BaseModel implements SearchContract.IModel {

    @Override
    public void collect(final boolean isCollect, String id, final OnCollectCallback onCollectCallback) {
        String url = "";
        if (isCollect) {
            url = Constant.URL_BASE + Constant.URL_COLLECT + id + "/json";
        } else {
            url = Constant.URL_BASE + Constant.URL_UNCOLLECT + id + "/json";
        }
        OkHttpUtil.getInstance().postAsync(url, null, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                onCollectCallback.collectResult(false, "取消收藏失败");
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    onCollectCallback.collectResult(isCollect, null);
                    return;
                }

                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    onCollectCallback.collectResult(false, "取消收藏失败");
                    return;
                }
                if (0 == msg.getErrorCode()) {
                    onCollectCallback.collectResult(true, "取消收藏成功");
                } else {
                    onCollectCallback.collectResult(false, "取消收藏失败");
                }
            }

        });
    }

    @Override
    public void search(String key, String page, final OnSearchCallback onSearchCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("k", key);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_SEARCH + page + "/json", map, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                onSearchCallback.error(e.toString());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                //                        Log.d("ssss", "onResponse: " + response);
                if (TextUtils.isEmpty(response)) {
                    onSearchCallback.error("服务器开小差了");
                } else {
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                    if (msg != null) {
                        if (0 == msg.getErrorCode()) {
                            String json = GsonUtil.gsonToJson(msg.getData());
                            ArticleData articleData = GsonUtil.gsonToBean(json, ArticleData.class);
                            if (articleData != null) {
                                onSearchCallback.getResult(articleData.getDatas());
                            }
                        } else {
                            onSearchCallback.error(msg.getErrorMsg().toString());
                        }
                    } else {
                        onSearchCallback.error("服务器开小差了," + msg.getErrorMsg());
                    }
                }
            }
        });
    }

    @Override
    public void getHotKey(final OnHotKeyCallback onHotKeyCallback) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_HOT_SEARCH, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                onHotKeyCallback.error(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    return;
                }
                if (msg.getErrorCode() == 0) {
                    String json = GsonUtil.gsonToJson(msg.getData());
                    List<HotKey> hotKeys = GsonUtil.gsonToList(json, HotKey.class);
                    if (hotKeys != null && hotKeys.size() > 0) {
                        onHotKeyCallback.hotKeyResult(hotKeys);
                    } else {
                        onHotKeyCallback.error("");
                    }
                } else {
                    onHotKeyCallback.error("");
                }
            }
        });
    }
}
