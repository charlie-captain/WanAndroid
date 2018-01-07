package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.MyBaseReclerViewAdapter;
import com.example.thatnight.wanandroid.base.SmartRecyclerViewHolder;

import java.util.Collection;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class SmartArticleAdapter extends MyBaseReclerViewAdapter {

    public SmartArticleAdapter(int layoutId) {
        super(layoutId);
    }

    public SmartArticleAdapter(Collection collection, int layoutId) {
        super(collection, layoutId);
    }

    public SmartArticleAdapter(Collection collection, int layoutId, AdapterView.OnItemClickListener listener) {
        super(collection, layoutId, listener);
    }

    @Override
    protected void onBindViewHolder(SmartRecyclerViewHolder holder, Object model, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }




    class ViewHolder extends SmartRecyclerViewHolder {
        TextView mTitle;
        TextView mAuthor;
        TextView mTime;
        TextView mType;
        ImageButton mIbLike;
        RelativeLayout mItem;

        public ViewHolder(View itemView, AdapterView.OnItemClickListener mListener) {
            super(itemView, mListener);
            mTitle = itemView.findViewById(R.id.item_tv_title);
            mAuthor = itemView.findViewById(R.id.item_tv_author);
            mTime = itemView.findViewById(R.id.item_tv_time);
            mType = itemView.findViewById(R.id.item_tv_type);
            mIbLike = itemView.findViewById(R.id.item_ib_like);
            mItem = itemView.findViewById(R.id.rl_item_news);
        }
    }
}
