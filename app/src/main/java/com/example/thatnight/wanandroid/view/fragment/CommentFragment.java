package com.example.thatnight.wanandroid.view.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.thatnight.wanandroid.BuildConfig;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.CommentAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Comment;
import com.example.thatnight.wanandroid.mvp.contract.CommentContract;
import com.example.thatnight.wanandroid.mvp.model.CommentModel;
import com.example.thatnight.wanandroid.mvp.presenter.CommentPresenter;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.SystemUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 评论页面
 */
public class CommentFragment extends BaseFragment<CommentContract.IView, CommentPresenter> implements CommentContract.IView, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, OnLoadmoreListener, OnRefreshListener, View.OnClickListener {


    private FloatingActionButton mAddButton;
    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mComments;
    private int mPage;

    @Override
    protected void initView() {

        mAddButton = (FloatingActionButton) $(R.id.fabtn_comment);
        mRv = (RecyclerView) $(R.id.rv_comment);
        mRefreshLayout = (RefreshLayout) $(R.id.srl_comment);
        mCommentAdapter = new CommentAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mCommentAdapter);
    }

    @Override
    protected void initData(Bundle arguments) {
        mComments = new ArrayList<>();

    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mCommentAdapter.setOnRecyclerViewListener(this);
        mAddButton.setOnClickListener(this);
        mPage = 0;
    }

    @Override
    protected void onLazyLoad() {
        mRefreshLayout.autoRefresh();

    }

    @Override
    protected CommentPresenter getPresenter() {
        return new CommentPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void showComment(boolean isRefresh, List<Comment> comments) {
        if (!isRefresh) {
            mComments.clear();
            mRefreshLayout.finishLoadmore(true);
        } else {
            mRefreshLayout.finishRefresh(true);
        }
        mComments.addAll(comments);
        mCommentAdapter.updateData(comments);

    }

    @Override
    public void success() {
        Snackbar.make(mRv, "成功发送反馈!", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void error(String error) {
        mRefreshLayout.finishRefresh(false);
        mRefreshLayout.finishLoadmore(false);
        Snackbar.make(mRv, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemLongClick(final int pos) {
        //admin
        if (AccountUtil.getAccount() != null && "thatnight".equals(AccountUtil.getAccount().getUsername())) {
            new android.app.AlertDialog.Builder(getActivity()).setMessage("删除" + mComments.get(pos).getContent()).setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mComments.get(pos).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtil.showToast("删除成功");
                            } else {
                                ToastUtil.showToast(e.toString());
                            }
                        }
                    });
                }
            }).setNegativeButton("否", null).show();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPresenter.getComment(false, ++mPage);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mPage != 0) {
            mPage = 0;
        }
        mPresenter.getComment(true, mPage);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("反馈");
        builder.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_write_comment, null);
        builder.setView(view);
        final EditText editText = (EditText) view.findViewById(R.id.et_comment_write);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    Comment comment = new Comment();
                    comment.setPhoneName("PHONE: " + SystemUtil.getDeviceBrand());
                    String userName = String.valueOf(SharePreferenceUtil.getInstance().optString("account"));
                    if ("null".equals(userName)) {
                        userName = "游客" + SystemUtil.getSystemModel() + SystemUtil.getDeviceBrand();
                    }
                    comment.setUserName(userName);
                    comment.setContent(editText.getText().toString().trim());
                    comment.setPhoneVersion("OS: " + SystemUtil.getSystemVersion());
                    comment.setVersion("APP:" + BuildConfig.VERSION_NAME);
                    mPresenter.addComment(comment);
                    editText.setText("");
                } else {
                    Snackbar.make(mRv, "输入内容不能为空", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                UiHelper.inputSoftWare(true, editText);
            }
        }, 300);
    }
}
