package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.ArticleComment;
import com.example.thatnight.wanandroid.utils.DateUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 评论Adapter
 * Created by ThatNight on 2018.1.7.
 */
public class ArticleCommentAdapter extends BaseRecyclerViewAdapter<ArticleComment> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_comment, null);
        return new ArticleCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArticleCommentHolder) holder).bindView(mDataList.get(position));
    }

    class ArticleCommentHolder extends BaseRvHolder {

        CircleImageView mIcon;
        TextView mUserName;
        TextView mContent;
        TextView mDateTime;

        public ArticleCommentHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.civ_article_comment);
            mUserName = itemView.findViewById(R.id.tv_article_comment_name);
            mContent = itemView.findViewById(R.id.tv_article_comment_content);
            mDateTime = itemView.findViewById(R.id.tv_article_comment_time);

        }

        @Override
        protected void bindView(ArticleComment comment) {
            if (comment == null || comment.getAccount() == null) {
                return;
            }
            if (comment.getAccount().getIcon() != null) {
                Glide.with(mIcon).load(comment.getAccount().getIcon().getUrl()).into(mIcon);
            }
            mUserName.setText(comment.getAccount().getNickName());
            mDateTime.setText(DateUtil.dateFormatDays(comment.getCreatedAt()));
            mContent.setText(comment.getContent());
        }
    }
}
