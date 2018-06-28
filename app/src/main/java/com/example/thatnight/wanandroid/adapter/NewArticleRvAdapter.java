package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;

;import java.util.regex.Pattern;

/**
 * 最新文章Adapter
 * Created by thatnight on 2017.10.27.
 */

public class NewArticleRvAdapter extends BaseRecyclerViewAdapter {

    private OnArticleItemClickListener mOnIbtnClickListener;
    private SparseBooleanArray mSelectArray = new SparseBooleanArray();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, null);
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
        TextView mType;
        ImageButton mIbLike;
        RelativeLayout mItem;

        public NewsHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_tv_title);
            mAuthor = itemView.findViewById(R.id.item_tv_author);
            mTime = itemView.findViewById(R.id.item_tv_time);
            mType = itemView.findViewById(R.id.item_tv_type);
            mIbLike = itemView.findViewById(R.id.item_ib_like);
            mItem = itemView.findViewById(R.id.rl_item_news);

        }


        @Override
        protected void bindView(Object o) {
            Article article = (Article) o;
            boolean isHtml = Pattern.matches(".*<em.+?>(.+?)</em>.*", article.getTitle());
            if (isHtml) {
                String textColor = "#FF4081";
                String newTitle = article.getTitle().replaceAll("<em.+?>", "<font color=\"" + textColor + "\">")
                        .replaceAll("</em>", "</font>");
                mTitle.setText(Html.fromHtml(newTitle));
            } else {
                mTitle.setText(article.getTitle());
            }

            mAuthor.setText(article.getAuthor());
            mTime.setText(article.getNiceDate());
            mType.setText(article.getChapterName());
            mIbLike.setTag(getLayoutPosition());
            mType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIbtnClickListener != null) {
                        mOnIbtnClickListener.onTypeClick(v, getLayoutPosition());
                    }
                }
            });
            mAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIbtnClickListener != null) {
                        mOnIbtnClickListener.onAuthorClick(v, getLayoutPosition());
                    }
                }
            });

            if (article.getOriginId() <= 0) {      //最新文章
                if (article.isCollect()) {
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
            } else {                        //收藏文章
                mIbLike.setTag(getLayoutPosition());
                mIbLike.setSelected(true);
                mIbLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnIbtnClickListener != null) {
                            mOnIbtnClickListener.onIbtnClick(v, getLayoutPosition());
                        }
                    }
                });
            }
        }

    }

    public void setOnIbtnClickListener(OnArticleItemClickListener onIbtnClickListener) {
        mOnIbtnClickListener = onIbtnClickListener;
    }

    public interface OnArticleItemClickListener {
        void onIbtnClick(View v, int position);

        void onTypeClick(View v, int position);

        void onAuthorClick(View v, int position);
    }


}
