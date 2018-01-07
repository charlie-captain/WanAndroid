package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.entity.Comment;

import java.util.List;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class CommentContract {
    public interface IView extends BaseContract.IBaseView {
        void showComment(boolean isRefresh, List<Comment> comments);

        void success();

        void error(String error);
    }

    public interface IModel extends BaseContract.IBaseModel {
        interface OnCommentListener {
            void success(boolean isRefresh, List<Comment> comments);

            void error(String error);
        }

        interface OnAddCommentListener {
            void isSuccess( boolean isSuccess, String error);
        }

        void addComment(Comment comment, OnAddCommentListener onAddCommentListener);

        void getComment(boolean isRefresh, int page, OnCommentListener onCommentListener);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getComment(boolean isRefresh,int page);

        void addComment(Comment comment);

    }
}
