package com.example.thatnight.wanandroid.mvp.model;


import android.text.TextUtils;

import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.WechatContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class WechatModel extends BaseFuncModel implements WechatContract.IModel {

    @Override
    public void getWechatParent(final MvpCallback callback) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_WECHAT, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    msg = DataHelper.obtainMsg(response);
                }
                callback.onResult(msg);
            }
        });
    }
}
