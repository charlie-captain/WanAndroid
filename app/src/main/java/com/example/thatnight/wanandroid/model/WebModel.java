package com.example.thatnight.wanandroid.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.contract.WebContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WebModel extends BaseModel implements WebContract.IWebModel {


    @Override
    public void getUrl(final boolean isCollect, String id, final WebContract.IWebPresenter iPresenter) {
        if (isCollect) {
            OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_COLLECT + id + "/json", new OkHttpResultCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(byte[] bytes) {
                    String response = new String(bytes);
                    if (TextUtils.isEmpty(response)) {
                        iPresenter.getResult(isCollect, null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                    iPresenter.getResult(isCollect, msg);
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
                        iPresenter.getResult(isCollect, null);
                    }
                    Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                    iPresenter.getResult(isCollect, msg);
                }

            }, null);
        }
    }
}
