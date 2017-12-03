package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;
import android.util.Log;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.ClassifyContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyModel extends BaseModel implements ClassifyContract.IModel {

    @Override
    public void getParentChildren(final ClassifyContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync("http://www.wanandroid.com/tree", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("jsoup", e.toString());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);

                Document document = Jsoup.parse(response);

                Elements list = document.select("div.list_sort");
                int i = 0;
                List<KeyValue> childList = new ArrayList<>();
                List<KeyValue> parentList = new ArrayList<>();
                List<List<KeyValue>> parentChildrenList = new ArrayList<>();

                for (Element element : list) {
                    Log.d("jsoup", "onResponse: " + element.toString());
                    Elements li = element.select("li");
                    Log.d("jsoup", "onResponse: " + li.toString());
                    if (i == 1) {
                        childList = new ArrayList<>();
                    }
                    for (Element e : li) {
                        String key = e.getElementsByTag("a").text();
                        String value = e.getElementsByTag("a").attr("href");
                        Log.d("jsoup", "onResponse: " + key + " " + value);
                        KeyValue keyValue = new KeyValue(key, value);
                        if (i == 0) {
                            parentList.add(keyValue);
                        } else {
                            childList.add(keyValue);
                        }
                    }
                    if (i == 1) {
                        parentChildrenList.add(childList);
                    }
                    i++;
                }
                iPresenter.setParentChildren(parentList, parentChildrenList);
            }
        });
    }

    @Override
    public void getChildren(String key, final ClassifyContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + key, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("jsoup", e.toString());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);

                Document document = Jsoup.parse(response);

                Elements list = document.select("div.list_sort");

                Elements li = list.get(1).select("li");
                List<KeyValue> mChildList = new ArrayList<>();
                for (Element e : li) {
                    String key = e.getElementsByTag("a").text();
                    String value = e.getElementsByTag("a").attr("href");
                    KeyValue keyValue = new KeyValue(key, value);
                    mChildList.add(keyValue);
                }
                iPresenter.getChildrenResult(mChildList);
            }
        });
    }

    @Override
    public void getArticle(final boolean isRefresh, int page, String value, final ClassifyContract.IPresenter iPresenter) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_ARTICLE + page + "/json" + value.substring(value.indexOf("?")), new OkHttpResultCallback() {
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
    public void collect(final boolean isCollect, String id, final ClassifyContract.IPresenter iPresenter) {
        if (isCollect) {
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT + id + "/json", new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(byte[] bytes) {
                    String response = new String(bytes);
                    if (TextUtils.isEmpty(response)) {
                        iPresenter.collectResult(isCollect, null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                    iPresenter.collectResult(isCollect, msg);
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
                        iPresenter.collectResult(isCollect, null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);

                    iPresenter.collectResult(isCollect, msg);
                }

            }, null);
        }

    }


}
