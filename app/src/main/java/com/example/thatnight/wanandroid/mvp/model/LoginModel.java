package com.example.thatnight.wanandroid.mvp.model;


import android.text.TextUtils;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.http.SaveCookieInterceptor;
import com.example.thatnight.wanandroid.mvp.contract.LoginContract;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by thatnight on 2017.11.1.
 */

public class LoginModel extends BaseModel implements LoginContract.ILoginModel {
    @Override
    public void login(String name, String pwd, final LoginContract.ILoginPresenter iLoginPresenter) {
        FormBody.Builder body = new FormBody.Builder()
                .add("username", name)
                .add("password", pwd);
        Request.Builder builder = new Request.Builder()
                .url(Constant.URL_BASE + Constant.URL_LOGIN)
                .post(body.build());

        OkHttpUtil.getLoginClient().newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iLoginPresenter.getResult(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responses = new String(response.body().bytes());
                if (TextUtils.isEmpty(responses)) {
                    iLoginPresenter.getResult(null);
                    return;

                }
                Msg msg = GsonUtil.gsonToBean(responses, Msg.class);
                iLoginPresenter.getResult(msg);
            }
        });
    }

    @Override
    public void register(String name, String pwd, final LoginContract.ILoginPresenter iLoginPresenter) {
        FormBody.Builder body = new FormBody.Builder().add("username", name).add("password", pwd)
                .add("repassword", pwd);
        Request.Builder builder = new Request.Builder()
                .url(Constant.URL_BASE + Constant.URL_REGISTER)
                .post(body.build());

        OkHttpUtil.getLoginClient().newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iLoginPresenter.getResult(null);
                CrashReport.postCatchedException(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responses = response.body().string();
                if (TextUtils.isEmpty(responses)) {
                    iLoginPresenter.getResult(null);
                    return;

                }
                Msg msg = GsonUtil.gsonToBean(responses, Msg.class);
                iLoginPresenter.getResult(msg);
            }
        });
    }
}
