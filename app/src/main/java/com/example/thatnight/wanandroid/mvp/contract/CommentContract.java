package com.example.thatnight.wanandroid.mvp.contract;

import com.example.thatnight.wanandroid.base.BaseContract;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.callback.MvpCallback;
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

        void addComment(Comment comment, MvpCallback callback);

        void getComment(boolean isRefresh, int page, MvpBooleanCallback callback);
    }

    public interface IPresenter extends BaseContract.IBasePresenter {
        void getComment(boolean isRefresh, int page);

        void addComment(Comment comment);

    }
}
