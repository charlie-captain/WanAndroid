package com.example.expandpopview.listview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.example.expandpopview.R;
import com.example.expandpopview.callback.IPopListView;
import com.example.expandpopview.callback.OnPopItemClickListener;
import com.example.expandpopview.callback.OnPopViewListener;
import com.example.expandpopview.callback.OnTwoListCallback;
import com.example.expandpopview.entity.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.5.
 */

public class PopTwoListView extends PopLinearLayout implements IPopListView {

    private ListView mParentListView;
    private ListView mChildListView;

    private List<KeyValue> mParentList;
    private List<KeyValue> mChildList;
    private List<List<KeyValue>> mParentChildren;

    private PopViewAdapter mParentAdapter;
    private PopViewAdapter mChildAdapter;
    private OnTwoListCallback mCallBack;
    private OnPopViewListener mOnPopViewListener;

    private int mParentPositionSelected;
    private int mParentPosition;
    private int mChildPosition;

    public PopTwoListView(Context context) {
        this(context, null);
    }

    public PopTwoListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopTwoListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.expend_tab_tow_list, this, true);
        mParentListView = findViewById(R.id.expand_lv_two_parent);
        mParentChildren = new ArrayList<>();
        mChildList = new ArrayList<>();
        mParentList = new ArrayList<>();
        mParentAdapter = new PopViewAdapter(context);
        mParentListView.setBackgroundColor(Color.WHITE);
        mParentAdapter.setSelectorBackground(Color.WHITE,
                getResources().getColor(R.color.color_selected_parent));

        mParentListView.setAdapter(mParentAdapter);
        mParentAdapter.setListener(new OnPopItemClickListener() {

            @Override
            public void onItemClick(PopViewAdapter adapter, int pos) {
                clickParent(adapter, pos);
            }
        });

        mChildListView = findViewById(R.id.expand_lv_two_children);
        mChildAdapter = new PopViewAdapter(context);
        mChildAdapter.setSelectorBackground(getResources().getColor(R.color.color_selected_parent),
                getResources().getColor(R.color.color_selected_child));
        mChildListView.setBackgroundColor(getResources().getColor(R.color.color_selected_parent));

        mChildListView.setAdapter(mChildAdapter);
        mChildAdapter.setListener(new OnPopItemClickListener() {
            @Override
            public void onItemClick(PopViewAdapter adapter, int pos) {
                clickChild(adapter, pos);
            }
        });
    }

    private void clickParent(PopViewAdapter adapter, int pos) {
        mParentPositionSelected = pos;
        adapter.setSelectPosition(pos);
        if (mCallBack != null) {
            mCallBack.returnParentKeyValue(pos, mParentList.get(pos));
        }
        mChildAdapter.setSelectPosition(-1);
        if (pos < mParentChildren.size()) {
            mChildList.clear();
            mChildList.addAll(mParentChildren.get(pos));
        }
        mChildAdapter.setKeyValueList(mChildList);
    }

    private void clickChild(PopViewAdapter adapter, int pos) {
        mChildPosition = pos;
        adapter.setSelectPosition(pos);
        mChildListView.setSelection(pos);
        if (mCallBack != null) {
            mCallBack.returnChildKeyValue(pos, mChildList.get(pos));
        }
        mParentListView.setSelection(mParentPositionSelected);
        mParentAdapter.setSelectPosition(mParentPositionSelected);
        mParentPosition = mParentPositionSelected;
        if (mOnPopViewListener != null) {
            mOnPopViewListener.unexpandPopView(mChildList.get(pos).getKey());
        }
    }

    public void refreshSelected() {
        if (mParentPosition != mParentPositionSelected) {
            mParentPositionSelected = mParentPosition;
            mParentAdapter.setSelectPosition(mParentPosition);
            mParentListView.setSelection(mParentPosition);
            //set child list
            if (mParentPosition < mParentChildren.size()) {
                mChildList.clear();
                mChildList.addAll(mParentChildren.get(mParentPosition));
                mChildAdapter.setKeyValueList(mChildList);
                mChildAdapter.setSelectPosition(mChildPosition);
                mChildListView.setSelection(mChildPosition);
            }
        }

    }

    public void setData(List<KeyValue> parentList, List<List<KeyValue>> parentChild) {
        setData(parentList, null, parentChild);
    }

    public void setData(List<KeyValue> parentList, List<KeyValue> childList, List<List<KeyValue>> parentChild) {
        if (parentChild != null) {
            mParentList.clear();
            mParentList.addAll(parentList);
        }
        if (parentChild != null) {
            mParentChildren.clear();
            mParentChildren.addAll(parentChild);
        }
        mChildList.clear();
        if (childList == null) {
            mChildList.addAll(parentChild.get(0));
        } else {
            mChildList.addAll(childList);
        }
        mParentAdapter.setKeyValueList(mParentList);
        mChildAdapter.setKeyValueList(mChildList);
    }

    public void setCallback(OnTwoListCallback callback) {
        if (callback != null) {
            mCallBack = callback;
        }
    }


    @Override
    public void setDrawable(int popViewTextSize, int popViewTextColor, int popViewTextColorSelected) {
        mParentAdapter.setTextColor(popViewTextColor, popViewTextColorSelected);
        mChildAdapter.setTextColor(popViewTextColor, popViewTextColorSelected);
        if (popViewTextSize != -1) {
            mParentAdapter.setTextSize(popViewTextSize);
            mChildAdapter.setTextSize(popViewTextSize);
        }
    }

    @Override
    public void setPopViewListener(OnPopViewListener listener) {
        mOnPopViewListener = listener;
    }
}
