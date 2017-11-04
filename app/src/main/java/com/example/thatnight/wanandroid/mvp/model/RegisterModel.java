package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.mvp.contract.RegisterContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class RegisterModel extends BaseModel implements RegisterContract.IModel {
    @Override
    public void register(String name, String pwd, final RegisterContract.IPresenter iPresenter) {
        Map<String, String> data = new HashMap<>();
        data.put("username", name);
        data.put("password", pwd);
        data.put("repassword", pwd);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_REGISTER, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iPresenter.getResult(null);
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iPresenter.getResult(msg);

            }
        }, data);
    }
}
