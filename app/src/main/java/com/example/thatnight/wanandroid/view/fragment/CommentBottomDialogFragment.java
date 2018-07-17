package com.example.thatnight.wanandroid.view.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.ArticleCommentAdapter;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.base.TinkerApp;
import com.example.thatnight.wanandroid.callback.LogoutState;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.ArticleComment;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * date: 2018/7/11
 * author: thatnight
 */
public class CommentBottomDialogFragment extends DialogFragment implements BaseRecyclerViewAdapter.OnClickRecyclerViewListener {


    private RecyclerView mRecyclerView;

    private ArticleCommentAdapter mCommentAdapter;

    private List<ArticleComment> mArticleComments;
    private Toolbar mToolbar;
    private TextView mTvEdit;
    private Article mArticle;
    public static final String KEY_ARTICLE = "article";


    public static CommentBottomDialogFragment newInstance(Article article) {
        CommentBottomDialogFragment commentBottomDialogFragment = new CommentBottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARTICLE, article);
        commentBottomDialogFragment.setArguments(bundle);
        return commentBottomDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.style_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.dialog_article_comment, null);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getData();
    }


    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.rv_article_comment);
        mTvEdit = view.findViewById(R.id.tv_article_comment);
        mToolbar = view.findViewById(R.id.tb);

        mCommentAdapter = new ArticleCommentAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mCommentAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 8);
            }
        });
        mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditView();
            }
        });
        mCommentAdapter.setOnRecyclerViewListener(this);
    }


    public void getData() {
        mArticle = getArguments().getParcelable(KEY_ARTICLE);

        if (mArticle == null) {
            dismiss();
        }
        mToolbar.setTitle(mArticle.getTitle());

        refreshData();
    }

    /**
     * 刷新列表
     */
    private void refreshData() {
        BmobQuery<ArticleComment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("articleId", mArticle.getId());
        commentBmobQuery.order("-createdAt");
        commentBmobQuery.include("account,toAccount");
        commentBmobQuery.findObjects(new FindListener<ArticleComment>() {
            @Override
            public void done(List<ArticleComment> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        return;
                    }
                    mArticleComments = list;
                    mCommentAdapter.updateData(mArticleComments);
                    if (mRecyclerView.getVisibility() == View.GONE) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(TinkerApp.getApplication(), "获取数据失败,稍后重试!", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemLongClick(int pos) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * 显示输入窗口
     */
    private void showEditView() {
        if (LoginContextUtil.getInstance().getUserState() instanceof LogoutState) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("评论");
        builder.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_write_comment, null);
        builder.setView(view);
        final EditText editText = view.findViewById(R.id.et_comment_write);
        editText.setHint("想说些什么...");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    ArticleComment comment = new ArticleComment();
                    if (AccountUtil.getBmobAccount() == null) {
                        return;
                    }
                    comment.setAccount(AccountUtil.getBmobAccount());
                    comment.setArticleId(mArticle.getId());
                    comment.setContent(editText.getText().toString().trim());
                    comment.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Snackbar.make(mRecyclerView, "评论成功!", Snackbar.LENGTH_SHORT).show();
                                refreshData();
                            } else {
                                Snackbar.make(mRecyclerView, "评论失败!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(mRecyclerView, "输入内容不能为空", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                UiHelper.inputSoftWare(true,editText);
            }
        },300);
    }
}
