package com.example.thatnight.wanandroid.data;

/**
 * date: 2018/9/3
 * author: thatnight
 */
public abstract class BaseDataController {

    protected boolean hasInit = false;

    public BaseDataController() {
    }

    protected void initData() {
        if (!hasInit) {
            getData();
            hasInit = true;
        }
    }

    protected abstract void getData();

}
