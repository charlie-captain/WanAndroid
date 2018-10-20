package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.WechatParent;
import com.example.thatnight.wanandroid.mvp.contract.WechatContract;
import com.example.thatnight.wanandroid.mvp.model.ProjectModel;
import com.example.thatnight.wanandroid.mvp.model.WechatModel;
import com.example.thatnight.wanandroid.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class WechatPresenter extends BaseFuncPresenter<WechatContract.IView> implements WechatContract.IPresenter {

    protected WechatModel mWechatModel;

    public WechatPresenter() {
        mWechatModel = new WechatModel();
    }

    @Override
    public void getWechatParent() {
        mWechatModel.getWechatParent(new MvpCallback() {
            @Override
            public void onResult(Msg msg) {
                if (msg == null) {
                    return;
                }

                if (view == null) {
                    return;
                }
                if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    List<WechatParent> wxParents = GsonUtil.gsonToList(msg.getData().toString(), WechatParent.class);
                    if (wxParents == null || wxParents.size() == 0) {
                        return;
                    }
                    List<KeyValue> parentList = new ArrayList<>();
                    for (WechatParent wxParent : wxParents) {
                        KeyValue keyValue = new KeyValue(wxParent.getName(), String.valueOf(wxParent.getId()));
                        parentList.add(keyValue);
                    }
                    view.setWechatParent(parentList);
                } else {


                }
            }
        });
    }
}
