package com.example.thatnight.wanandroid.mvp.presenter;

import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Comment;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.CommentContract;
import com.example.thatnight.wanandroid.mvp.model.CommentModel;
import com.example.thatnight.wanandroid.view.fragment.CommentFragment;

import java.util.List;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class CommentPresenter extends BasePresenter<CommentFragment> implements CommentContract.IPresenter {

    protected CommentModel mCommentModel;

    public CommentPresenter() {
        mCommentModel = new CommentModel();
    }

    @Override
    public void getComment(final boolean isRefresh, int page) {
        if (mCommentModel == null) {
            // TODO: 2018.1.10 解决Activity退出而引用空指针
            return; //防止activity退出, 而空指针
        }
        mCommentModel.getComment(isRefresh, page, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                if(msg.getErrorCode()==Constant.CODE_SUCCESS){
                    view.showComment(isRefresh, (List<Comment>) msg.getData());
                }else{
                    view.error(msg.getErrorMsg().toString());
                }
            }
        });
    }

    @Override
    public void addComment(Comment comment) {
        mCommentModel.addComment(comment, new MvpCallback() {
            @Override
            public void onResult(Msg msg) {
                if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    view.success();
                } else {
                    view.error(msg.getErrorMsg().toString());
                }
            }
        });
    }


}
