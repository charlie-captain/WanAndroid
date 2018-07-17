package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.ClassifyContract;
import com.example.thatnight.wanandroid.mvp.model.ClassifyModel;
import com.example.thatnight.wanandroid.utils.DataHelper;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyPresenter extends BaseFuncPresenter<ClassifyContract.IView> implements ClassifyContract.IPresenter {

    private ClassifyModel mClassifyModel;

    public ClassifyPresenter() {
        mClassifyModel = new ClassifyModel();
    }

    @Override
    public void getParentChildren() {
        mClassifyModel.getParentChildren(new MvpCallback() {
            @Override
            public void onResult(Msg msg) {
                if (view == null) {
                    return;
                }
                if (DataHelper.isNullOrEmpty(msg)) {
                    view.showToast(Constant.STRING_ERROR);
                } else {
                    view.setExpandPopView((List<KeyValue>) msg.getData(), (List<List<KeyValue>>) msg.getData2());
                }
            }
        });
    }

    @Override
    public void getArticle(boolean isRefresh, int page, String value) {
        mClassifyModel.getArticle(Constant.URL_BASE + Constant.URL_ARTICLE + page + "/json" + "?cid=" + value, isRefresh, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                returnArticle(msg, b);
            }
        });
    }
}
