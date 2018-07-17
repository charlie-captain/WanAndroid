package com.example.thatnight.wanandroid.base;

import java.util.List;

/**
 * date: 2018/7/17
 * author: thatnight
 */
public interface BaseFuncView extends BaseContract.IBaseView {

    void onCollect(boolean isSuccess, String s);

    <T> void refreshData(List<T> datas);

    <T> void loadmoreData(List<T> datas);
}
