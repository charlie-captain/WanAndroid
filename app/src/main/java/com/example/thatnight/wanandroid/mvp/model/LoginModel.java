package com.example.thatnight.wanandroid.mvp.model;


import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.LoginContract;
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

public class LoginModel extends BaseModel implements LoginContract.ILoginModel {
    @Override
    public void login(String name, String pwd, final LoginContract.ILoginPresenter iLoginPresenter) {
        Map<String, String> data = new HashMap<>();
        data.put("username", name);
        data.put("password", pwd);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_LOGIN, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iLoginPresenter.getResult(null);
                CrashReport.postCatchedException(e);
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iLoginPresenter.getResult(null);
                    return;

                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iLoginPresenter.getResult(msg);
            }
        }, data);
    }

    @Override
    public void register(String name, String pwd, final LoginContract.ILoginPresenter iLoginPresenter) {
        Map<String, String> data = new HashMap<>();
        data.put("username", name);
        data.put("password", pwd);
        data.put("repassword", pwd);
        OkHttpUtil.getInstance().postAsync(Constant.URL_BASE + Constant.URL_REGISTER, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iLoginPresenter.getResult(null);
                CrashReport.postCatchedException(e);

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    iLoginPresenter.getResult(null);
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                iLoginPresenter.getResult(msg);

            }
        }, data);
    }
}
