package com.example.thatnight.wanandroid.callback;

import java.util.List;

/**
 * date: 2018/7/17
 * author: thatnight
 */
public interface MvpListCallback {
    <T> void onResult(List<T> datas);
}
