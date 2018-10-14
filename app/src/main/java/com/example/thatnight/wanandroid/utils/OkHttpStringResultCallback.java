package com.example.thatnight.wanandroid.utils;


import android.telecom.Call;

public interface OkHttpStringResultCallback {

    void onError(Call call, Exception e);

    void onResponse(String data);
}
