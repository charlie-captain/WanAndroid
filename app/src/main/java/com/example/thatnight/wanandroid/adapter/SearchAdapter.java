package com.example.thatnight.wanandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by ThatNight on 2017.12.23.
 */

public class SearchAdapter extends BaseRecyclerViewAdapter<String> {

    public List<String> getData() {
        return mDataList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((Holder) holder).bindView(mDataList.get(position));

    }

    public class Holder extends BaseRvHolder {
        TextView tv;

        public Holder(View v) {
            super(v);
            tv = v.findViewById(R.id.tv_search);
        }

        @Override
        protected void bindView(String s) {
            tv.setText(s);
        }
    }
}
