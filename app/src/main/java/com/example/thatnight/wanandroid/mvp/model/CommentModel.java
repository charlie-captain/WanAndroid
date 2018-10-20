package com.example.thatnight.wanandroid.mvp.model;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Comment;
import com.example.thatnight.wanandroid.mvp.contract.CommentContract;
import com.example.thatnight.wanandroid.utils.DataHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class CommentModel extends BaseModel implements CommentContract.IModel {

    @Override
    public void addComment(final Comment comment, final MvpCallback callback) {
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    callback.onResult(DataHelper.obtainMsg(e));
                } else {
                    callback.onResult(DataHelper.obtainErrorMsg(e));
                }
            }
        });
    }

    @Override
    public void getComment(final boolean isRefresh, int page, final MvpBooleanCallback callback) {
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.order("-createdAt");
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    callback.onResult(isRefresh, DataHelper.obtainMsg(list));
                } else {
                    callback.onResult(isRefresh, DataHelper.obtainErrorMsg(e));
                }
            }
        });
    }
}
