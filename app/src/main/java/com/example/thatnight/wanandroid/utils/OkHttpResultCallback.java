package com.example.thatnight.wanandroid.utils;


import cn.bmob.v3.okhttp3.Call;

public interface  OkHttpResultCallback {

    void onError(Call call, Exception e);

    void onResponse(byte[] bytes);
}
