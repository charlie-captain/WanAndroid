package com.example.thatnight.wanandroid.mvp.model;

import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.entity.Comment;
import com.example.thatnight.wanandroid.mvp.contract.CommentContract;

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
    public void addComment(final Comment comment, final OnAddCommentListener onAddCommentListener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("mUserName", comment.getUserName());
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    comment.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                onAddCommentListener.isSuccess(true, null);
                            } else {
                                onAddCommentListener.isSuccess(false, "服务器开小差了," + e.toString());
                            }
                        }
                    });
                } else {
                    comment.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                onAddCommentListener.isSuccess(true, null);
                            } else {
                                onAddCommentListener.isSuccess(false, "服务器开小差了," + e.toString());
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    public void getComment(final boolean isRefresh, int page, final OnCommentListener onCommentListener) {
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.order("-createdAt");
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    onCommentListener.success(isRefresh, list);
                } else {
                    onCommentListener.error("服务器开小差了, " + e.toString());
                }
            }
        });
    }

}
