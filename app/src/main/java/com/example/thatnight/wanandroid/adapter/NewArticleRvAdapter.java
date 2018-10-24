package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.ArticleComment;

;import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

/**
 * 最新文章Adapter
 * Created by thatnight on 2017.10.27.
 */

public class NewArticleRvAdapter extends BaseRecyclerViewAdapter {

    private OnArticleItemClickListener mOnIbtnClickListener;
    private SparseBooleanArray mSelectArray = new SparseBooleanArray();
    private SparseIntArray mCommentArray = new SparseIntArray();

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
        TextView mComment;
        ImageButton mIbLike;
        ImageButton mIbComment;


        public NewsHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_tv_title);
            mAuthor = itemView.findViewById(R.id.item_tv_author);
            mTime = itemView.findViewById(R.id.item_tv_time);
            mType = itemView.findViewById(R.id.item_tv_type);
            mComment = itemView.findViewById(R.id.item_tv_comment);
            mIbLike = itemView.findViewById(R.id.item_ib_like);
            mIbComment = itemView.findViewById(R.id.item_ibtn_comment);

        }


        @Override
        protected void bindView(Object o) {
            Article article = (Article) o;
            boolean isHtml = Pattern.matches(".*<em.+?>(.+?)</em>.*", article.getTitle());
            if (isHtml) {
                String textColor = "#FF4081";
                String newTitle = article.getTitle().replaceAll("<em.+?>", "<font color=\"" + textColor + "\">").replaceAll("</em>", "</font>");
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
            mIbComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIbtnClickListener != null) {
                        mOnIbtnClickListener.onCommentClick(v, getLayoutPosition());
                    }
                }
            });

// TODO: 2018.10.20 评论查询功能暂时不开放
//            BmobQuery<ArticleComment> query = new BmobQuery<>();
//            query.addWhereEqualTo("articleId", article.getId());
//            query.count(ArticleComment.class, new CountListener() {
//                @Override
//                public void done(Integer integer, BmobException e) {
//                    if (e == null) {
//                        mCommentArray.put(getLayoutPosition(), integer);
//                        mComment.setText(String.valueOf(integer));
//                    }
//                }
//            });
//            mComment.setText(String.valueOf(mCommentArray.get(getLayoutPosition())));
            mComment.setVisibility(View.GONE);


            if (article.isOther()) {
                //如果是导航文章，不显示收藏
                mIbLike.setVisibility(View.GONE);
            } else {
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

    }

    public void setOnIbtnClickListener(OnArticleItemClickListener onIbtnClickListener) {
        mOnIbtnClickListener = onIbtnClickListener;
    }

    public interface OnArticleItemClickListener {
        void onIbtnClick(View v, int position);

        void onTypeClick(View v, int position);

        void onAuthorClick(View v, int position);

        void onCommentClick(View v, int position);
    }


}
