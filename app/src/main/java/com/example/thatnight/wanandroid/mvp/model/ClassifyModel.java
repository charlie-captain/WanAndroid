package com.example.thatnight.wanandroid.mvp.model;

import android.text.TextUtils;

import com.example.expandpopview.entity.KeyValue;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.ArticleData;
import com.example.thatnight.wanandroid.entity.ChildrenNode;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.entity.TreeNode;
import com.example.thatnight.wanandroid.mvp.contract.ClassifyContract;
import com.example.thatnight.wanandroid.utils.DataHelper;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by thatnight on 2017.11.1.
 */

public class ClassifyModel extends BaseFuncModel implements ClassifyContract.IModel {

    @Override
    public void getParentChildren(final MvpCallback callback) {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_TREE, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                CrashReport.postCatchedException(e);

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                if (TextUtils.isEmpty(response)) {
                    callback.onResult(DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                    return;
                }
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg == null) {
                    callback.onResult(DataHelper.obtainErrorMsg(Constant.STRING_ERROR));
                } else {
                    String json = GsonUtil.gsonToJson(msg.getData());
                    List<TreeNode> treeNodes = GsonUtil.gsonToList(json, TreeNode.class);
                    if (treeNodes != null) {
                        List<KeyValue> childList = new ArrayList<>();
                        List<KeyValue> parentList = new ArrayList<>();
                        List<List<KeyValue>> parentChildrenList = new ArrayList<>();

                        for (TreeNode t : treeNodes) {
                            String parentkey = t.getName();
                            String parentvalue = String.valueOf(t.getId());
                            KeyValue parentKeyValue = new KeyValue(parentkey, parentvalue);
                            parentList.add(parentKeyValue);

                            if (t.getChildren() != null) {
                                childList = new ArrayList<>();
                                for (ChildrenNode c : t.getChildren()) {
                                    String key = c.getName();
                                    String value = String.valueOf(c.getId());
                                    KeyValue keyValue = new KeyValue(key, value);
                                    childList.add(keyValue);
                                }
                                parentChildrenList.add(childList);

                            }
                        }
                        msg.setErrorCode(Constant.CODE_SUCCESS);
                        msg.setData(parentList);
                        msg.setData2(parentChildrenList);
                        callback.onResult(msg);
                    }
                }
            }
        });
    }

}
