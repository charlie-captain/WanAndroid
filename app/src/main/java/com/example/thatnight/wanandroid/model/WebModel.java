package com.example.thatnight.wanandroid.model;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.bean.Msg;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.contract.LoginContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WebModel extends BaseModel implements LoginContract.ILoginModel {
    @Override
    public void login(String name, String pwd, final LoginContract.ILoginPresenter iLoginPresenter) {
        Map<String, String> data = new HashMap<>();
        data.put("username", name);
        data.put("password", pwd);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_LOGIN, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iLoginPresenter.getResult(null);
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iLoginPresenter.getResult(msg);

            }
        }, data);
    }
}
