package com.example.thatnight.wanandroid.utils;


import com.example.thatnight.wanandroid.entity.Msg;

import cn.bmob.v3.okhttp3.Call;

public interface OkHttpStringResultCallback {

    void onError(Call call, Exception e);

    void onResponse(String data);
}
