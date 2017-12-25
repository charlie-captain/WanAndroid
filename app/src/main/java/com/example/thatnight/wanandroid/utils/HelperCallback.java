package com.example.thatnight.wanandroid.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;

/**
 * Created by thatnight on 2017.11.14.
 */

public class HelperCallback extends ItemTouchHelper.Callback {

    private BaseRecyclerViewAdapter mAdapter;

    public HelperCallback(BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP |
                ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        mAdapter.swapData(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.swipedDelete(viewHolder.getAdapterPosition());
    }


}
