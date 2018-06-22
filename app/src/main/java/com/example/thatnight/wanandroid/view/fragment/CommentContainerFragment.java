package com.example.thatnight.wanandroid.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.thatnight.wanandroid.base.BaseContainerFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;

/**
 * date: 2018/6/22
 * author: thatnight
 */
public class CommentContainerFragment extends BaseContainerFragment {
    @Override
    protected BaseModel initModel() {
        return null;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }


    @Override
    protected Fragment initSupportFragment() {
        return new CommentFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("反馈");
        setDraw(true);
    }
}
