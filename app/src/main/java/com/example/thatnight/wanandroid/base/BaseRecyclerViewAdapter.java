package com.example.thatnight.wanandroid.base;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mDataList = new ArrayList<>();
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;

    //更新数据
    public void updateData(List dataList) {
        mDataList.clear();
        appendData(dataList);
    }

    //分页加载，追加数据
    public void appendData(List dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        } else if (dataList != null && dataList.isEmpty()) {
            notifyDataSetChanged();
            //空数据更新
        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        mOnRecyclerViewListener = onRecyclerViewListener;
    }

    public abstract class BaseRvHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public BaseRvHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        protected abstract void bindView(T t);

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemLongClick(getLayoutPosition());
                return true;
            }
            return false;
        }
    }

    public interface OnClickRecyclerViewListener {
        void onItemClick(int pos);

        abstract void onItemLongClick(int pos);

    }

}

