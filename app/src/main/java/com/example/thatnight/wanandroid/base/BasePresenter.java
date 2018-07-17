package com.example.thatnight.wanandroid.base;

/**
 * Created by thatnight on 2017.11.1.
 */

public class BasePresenter<V extends BaseContract.IBaseView> implements BaseContract.IBasePresenter {

    public V view;

    void attachView(V v) {
        this.view = v;
    }

    void detachView() {
        view = null;
    }

    public BasePresenter() {
    }
}
