package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.MoreContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.MoreDataHelper;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.List;

import okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class MoreModel extends BaseModel implements MoreContract.IModel {
    @Override
    public void getArticle(final boolean isRefresh, String url, final int mode, int page, final MvpBooleanCallback callback) {
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(isRefresh, null);
                    return;
                }

                List<Article> articles = MoreDataHelper.html2ArticleList(mode, response);

                Msg msg = new Msg();
                msg.setErrorCode(Constant.CODE_SUCCESS);
                msg.setData(articles);
                callback.onResult(isRefresh, msg);
            }
        });
    }

    @Override
    public void collect(final boolean isCollect, Article article, final MvpBooleanCallback callback) {

    }
}
