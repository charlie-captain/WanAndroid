package com.example.thatnight.wanandroid.mvp.contract;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.base.BaseFuncView;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WechatContract {

    public interface IView extends BaseFuncView {

        void setWechatParent(List<KeyValue> parents);
    }

    public interface IPresenter extends BaseFuncContract.IBasePresenter {

        void getWechatParent();

    }

    public interface IModel extends BaseFuncContract.IBaseModel {

        void getWechatParent(MvpCallback callback);
    }
}
