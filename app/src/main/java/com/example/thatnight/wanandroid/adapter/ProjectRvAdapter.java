package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.Project;
import com.example.thatnight.wanandroid.entity.ProjectItem;

import java.util.regex.Pattern;

;

/**
 * 最新文章Adapter
 * Created by thatnight on 2017.10.27.
 */

public class ProjectRvAdapter extends BaseRecyclerViewAdapter<ProjectItem> {

    private OnArticleItemClickListener mOnIbtnClickListener;
    private SparseBooleanArray mSelectArray = new SparseBooleanArray();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, null);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NewsHolder) holder).bindView(mDataList.get(position));
    }

    class NewsHolder extends BaseRvHolder {

        TextView mTitle;
        TextView mAuthor;
        TextView mTime;
        ImageButton mIbLike;
        ImageView mImageView;
        RelativeLayout mItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_tv_title);
            mAuthor = itemView.findViewById(R.id.item_tv_author);
            mTime = itemView.findViewById(R.id.item_tv_time);
            mIbLike = itemView.findViewById(R.id.item_ib_like);
            mItem = itemView.findViewById(R.id.rl_item_news);
            mImageView = itemView.findViewById(R.id.iv_project);

        }


        @Override
        protected void bindView(ProjectItem projectItem) {
            boolean isHtml = Pattern.matches(".*<em.+?>(.+?)</em>.*", projectItem.getTitle());
            if (isHtml) {
                String textColor = "#FF4081";
                String newTitle = projectItem.getTitle().replaceAll("<em.+?>", "<font color=\"" + textColor + "\">").replaceAll("</em>", "</font>");
                mTitle.setText(Html.fromHtml(newTitle));
            } else {
                mTitle.setText(projectItem.getTitle());
            }

            //项目图
            Glide.with(mImageView).load(projectItem.getEnvelopePic()).into(mImageView);

            mAuthor.setText(projectItem.getAuthor());
            mTime.setText(projectItem.getNiceDate());
            mIbLike.setTag(getLayoutPosition());
            mAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIbtnClickListener != null) {
                        mOnIbtnClickListener.onAuthorClick(v, getLayoutPosition());
                    }
                }
            });

            if (projectItem.isCollect()) {
                mIbLike.setSelected(true);
                mSelectArray.put(getLayoutPosition(), true);
            } else {
                mIbLike.setSelected(false);
                mSelectArray.put(getLayoutPosition(), false);
            }

            mIbLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        mSelectArray.put(getLayoutPosition(), false);
                    } else {
                        mSelectArray.put(getLayoutPosition(), true);
                    }
                    if (mOnIbtnClickListener != null) {
                        mOnIbtnClickListener.onIbtnClick(v, getLayoutPosition());
                    }
                }
            });

        }

    }

    public void setOnIbtnClickListener(OnArticleItemClickListener onIbtnClickListener) {
        mOnIbtnClickListener = onIbtnClickListener;
    }

    public interface OnArticleItemClickListener {
        void onIbtnClick(View v, int position);


        void onAuthorClick(View v, int position);
    }


}
