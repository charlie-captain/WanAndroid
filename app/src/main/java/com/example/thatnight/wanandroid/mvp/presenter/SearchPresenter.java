package com.example.thatnight.wanandroid.mvp.presenter;

import android.support.v4.app.NavUtils;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.mvp.model.SearchModel;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchPresenter extends BasePresenter<SearchModel, SearchContract.IView> implements SearchContract.IPresenter {

    @Override
    public void search(final boolean isRefresh, String key, String page) {
        model.search(key, page, new SearchContract.IModel.OnSearchCallback() {
            @Override
            public void getResult(List<Article> articles) {
                if (view != null) {
                    view.showArticles(isRefresh, articles);
                }
            }

            @Override
            public void error(String s) {
                if (view != null) {
                    view.error(s);
                }
            }
        });
    }

    @Override
    public void collect(boolean isCollect, String id) {
        model.collect(isCollect, id, new SearchContract.IModel.OnCollectCallback() {
            @Override
            public void collectResult(boolean isCollect, String error) {
                if (view != null) {
                    view.isCollectSuccess(isCollect, error);
                }
            }
        });
    }

    @Override
    public void getHotKey() {
        model.getHotKey(new SearchContract.IModel.OnHotKeyCallback() {
            @Override
            public void hotKeyResult(List<HotKey> hotKeys) {
                if (view != null) {
                    view.showHotKey(hotKeys);
                }
            }

            @Override
            public void error(String error) {

            }
        });
    }
}
