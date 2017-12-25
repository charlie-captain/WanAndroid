package com.example.expandpopview.listview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.example.expandpopview.R;
import com.example.expandpopview.callback.IPopListView;
import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.callback.OnPopItemClickListener;
import com.example.expandpopview.callback.OnPopViewListener;
import com.example.expandpopview.entity.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.5.
 */

public class PopOneListView extends PopLinearLayout implements IPopListView {

    private ListView mOneListView;

    private List<KeyValue> mOneList;

    private PopViewAdapter mListAdapter;
    private OnOneListCallback mCallBack;
    private OnPopViewListener mOnPopViewListener;


    public PopOneListView(Context context) {
        this(context, null);
    }

    public PopOneListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopOneListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.expend_tab_one_list, this, true);
        mOneListView = findViewById(R.id.expand_lv_one);
        mOneList = new ArrayList<>();
        mListAdapter = new PopViewAdapter(context);
        mListAdapter.setSelectorBackground(Color.WHITE, getResources().getColor(R.color.color_selected_parent));
        mOneListView.setAdapter(mListAdapter);
        mListAdapter.setListener(new OnPopItemClickListener() {
            @Override
            public void onItemClick(PopViewAdapter adapter, int pos) {
                mListAdapter.setSelectPosition(pos);
                if (mCallBack != null) {
                    mCallBack.returnKeyValue(pos, mOneList.get(pos));
                }
                if (mOnPopViewListener != null) {
                    mOnPopViewListener.unexpandPopView(mOneList.get(pos).getKey());
                }
            }
        });

    }

    @Override
    public void setDrawable(int popViewTextSize, int popViewTextColor, int popViewTextColorSelected) {
        mListAdapter.setTextColor(popViewTextColor, popViewTextColorSelected);
        if (popViewTextSize != -1) {
            mListAdapter.setTextSize(popViewTextSize);
        }
    }

    @Override
    public void setPopViewListener(OnPopViewListener listener) {
        mOnPopViewListener = listener;
    }


    public void setCallback(OnOneListCallback callback) {
        if (callback != null) {
            mCallBack = callback;
        }
    }

    public void setData(List<KeyValue> oneList) {
        mOneList.addAll(oneList);
        mListAdapter.setKeyValueList(mOneList);
    }


}
