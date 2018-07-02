package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Comment;

/**
 * 评论Adapter
 * Created by ThatNight on 2018.1.7.
 */
public class CommentAdapter extends BaseRecyclerViewAdapter<Comment> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, null);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentHolder) holder).bindView(mDataList.get(position));
    }

    class CommentHolder extends BaseRvHolder {

        TextView mUserName;
        TextView mPhoneName;
        TextView mPhoneVersion;
        TextView mContent;
        TextView mDateTime;
        TextView mAppVersion;

        public CommentHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.tv_comment_name);
            mPhoneVersion = itemView.findViewById(R.id.tv_comment_phone_version);
            mPhoneName = itemView.findViewById(R.id.tv_comment_phone);
            mContent = itemView.findViewById(R.id.tv_comment_content);
            mDateTime = itemView.findViewById(R.id.tv_comment_date);
            mAppVersion = itemView.findViewById(R.id.tv_comment_app_version);

        }

        @Override
        protected void bindView(Comment comment) {
            mUserName.setText(comment.getUserName());
            mPhoneName.setText(comment.getPhoneName());
            mPhoneVersion.setText(comment.getPhoneVersion());
            mAppVersion.setText(comment.getVersion());
            mDateTime.setText(comment.getCreatedAt().split(" ")[0]);
            mContent.setText(comment.getContent());
        }
    }
}
