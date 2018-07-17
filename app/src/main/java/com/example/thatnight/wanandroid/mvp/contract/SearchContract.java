package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.entity.Msg;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.16.
 */

public class SearchContract {
    public interface IPresenter extends BaseContract.IBasePresenter {
        void search(boolean isRefresh, String key, String page);

        void getHotKey();
    }

    public interface IView extends BaseContract.IBaseView, BaseFuncView {
        void showHotKey(List<HotKey> hotKeys);

        void error(String s);
    }

    public interface IModel extends BaseContract.IBaseModel {

        void search(String key, String page, MvpCallback onSearchCallback);

        void getHotKey(MvpCallback onHotKeyCallback);
    }

}
