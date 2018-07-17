package com.example.thatnight.wanandroid.mvp.presenter;

import android.support.v4.app.NavUtils;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.mvp.model.SearchModel;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchPresenter extends BaseFuncPresenter<SearchContract.IView> implements SearchContract.IPresenter {

    private SearchModel mSearchModel;

    public SearchPresenter() {
        mSearchModel = new SearchModel();
    }

    @Override
    public void search(final boolean isRefresh, String key, String page) {
        mSearchModel.search(key, page, new MvpCallback() {
            @Override
            public void onResult(Msg msg) {
                returnArticle(msg, true);
            }
        });
    }

    @Override
    public void getHotKey() {
        mSearchModel.getHotKey(new MvpCallback() {
            @Override
            public void onResult(Msg msg) {
                if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    view.showHotKey((List<HotKey>) msg.getData());
                } else {
                    view.showToast(msg.getErrorMsg().toString());
                }
            }
        });
    }
}
