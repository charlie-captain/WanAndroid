package com.example.thatnight.wanandroid.base;

/**
 * Created by thatnight on 2017.11.1.
 */

public class BasePresenter<M extends BaseModel, V extends BaseContract.IBaseView> {

    public M model;
    public V view;

    void attachView(M m, V v) {
        this.model = m;
        this.view = v;
    }

    void detachView() {
        model = null;
        view = null;
    }
}
