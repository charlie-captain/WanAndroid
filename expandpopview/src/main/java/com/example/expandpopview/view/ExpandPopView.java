package com.example.expandpopview.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.expandpopview.R;
import com.example.expandpopview.callback.OnOneListCallback;
import com.example.expandpopview.callback.OnPopViewListener;
import com.example.expandpopview.callback.OnTwoListCallback;
import com.example.expandpopview.entity.KeyValue;
import com.example.expandpopview.listview.PopLinearLayout;
import com.example.expandpopview.listview.PopOneListView;
import com.example.expandpopview.listview.PopTwoListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thatnight on 2017.11.5.
 */

public class ExpandPopView extends LinearLayout implements PopupWindow.OnDismissListener, OnPopViewListener {
    protected List<RelativeLayout> mViews;
    protected ToggleButton mTbSelected;
    protected FixedPopupWindow mPopupWindow;
    protected Context mContext;
    protected int mDisplayWidth;
    protected int mDisplayHeight;

    protected int mSelectPosition;
    protected int mTabPosition = -1;

    protected int mTbtnBackground;            //togglebutton bg
    protected int mTbtnBackgroundColor;       //togglebutton bgcolor
    protected int mTbtnTextColor;             //togglebutton textcolor
    protected int mTbtnTextSize;              //togglebutton textsize
    protected int mPopViewBackgroundColor;    //popview bgcolor
    protected int mPopViewTextSize;           //popview text size
    protected int mPopViewTextColor;          //popview text color
    protected int mPopViewTextColorSelected;          //popview text color

    protected PopTwoListView mPopTwoListView;
    protected List<Integer> mTypeList;
    protected static final int TYPE_ONE = 1;
    protected static final int TYPE_TWO = 2;
    protected List<ToggleButton> mToggleButtons;
    protected Map<Integer, PopTwoListView> mTwoListMap;
    protected Map<Integer, PopOneListView> mOneListMap;

    public ExpandPopView(Context context) {
        this(context, null);
    }

    public ExpandPopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandPopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mViews = new ArrayList<>();
        mTypeList = new ArrayList<>();
        mTwoListMap = new HashMap<>();
        mOneListMap = new HashMap<>();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandPopView);
        mTbtnBackground = a.getResourceId(R.styleable.ExpandPopView_tab_togglebtn_bg, -1);
        mTbtnBackgroundColor = a.getColor(R.styleable.ExpandPopView_tab_togglebtn_bg_color, Color.WHITE);

        mTbtnTextSize = a.getDimensionPixelSize(R.styleable.ExpandPopView_tab_togglebtn_text_size, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14,
                getResources().getDisplayMetrics()));

        mTbtnTextColor = a.getColor(R.styleable.ExpandPopView_tab_togglebtn_text_color, Color.BLACK);

        mPopViewBackgroundColor = a.getColor(R.styleable.ExpandPopView_tab_pop_bg_color,
                Color.parseColor("#b0000000"));

        mPopViewTextSize = a.getDimensionPixelSize(R.styleable.ExpandPopView_tab_pop_text_size,
                mTbtnTextSize - (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        1,
                        getResources().getDisplayMetrics()));

        mPopViewTextColor = a.getColor(R.styleable.ExpandPopView_tab_pop_text_color, Color.BLACK);
        mPopViewTextColorSelected = a.getColor(R.styleable.ExpandPopView_tab_pop_text_color_selected,
                0);
        a.recycle();
        mDisplayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        mDisplayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        setOrientation(HORIZONTAL);
    }

    /**
     * add onelistview to tab
     *
     * @param title
     * @param oneList
     */
    public void addItemToExpandTab(String title, List<KeyValue> oneList, OnOneListCallback callback) {
        PopOneListView oneListView = new PopOneListView(mContext);
        oneListView.setData(oneList);
        oneListView.setCallback(callback);
        oneListView.setPopViewListener(this);
        oneListView.setDrawable(mPopViewTextSize, mPopViewTextColor, mPopViewTextColorSelected);
        mTypeList.add(TYPE_ONE);
        mOneListMap.put(mTabPosition + 1, oneListView);
        addItemToExpandTab(title, oneListView);
    }

    /**
     * add twolistview to tab
     *
     * @param title
     * @param parentList
     * @param parentChild
     */
    public void addItemToExpandTab(String title, List<KeyValue> parentList,
                                   List<List<KeyValue>> parentChild,
                                   OnTwoListCallback callback) {
        PopTwoListView twoListView = new PopTwoListView(mContext);
        twoListView.setData(parentList, parentChild);
        twoListView.setCallback(callback);
        twoListView.setPopViewListener(this);
        twoListView.setDrawable(mPopViewTextSize, mPopViewTextColor, mPopViewTextColorSelected);
        mTypeList.add(TYPE_TWO);
        mTwoListMap.put(mTabPosition + 1, twoListView);
        addItemToExpandTab(title, twoListView);
    }

    public void addItemToExpandTab(String title, PopLinearLayout tabItemView) {
        if (mToggleButtons == null) {
            mToggleButtons = new ArrayList<>();
        }
        ToggleButton tBtn = (ToggleButton) LayoutInflater.from(mContext).
                inflate(R.layout.expand_tab_togglebutton, this, false);
        setToggleButtonDrawable(tBtn);
        tBtn.setText(title);
        tBtn.setTextOff(title);
        tBtn.setTextOn(title);
        mTabPosition += 1;
        tBtn.setTag(mTabPosition);
        tBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton toggleButton = (ToggleButton) v;
                if (mTbSelected != null && mTbSelected != toggleButton) {
                    mTbSelected.setChecked(false);
                }
                mTbSelected = toggleButton;
                mSelectPosition = (int) mTbSelected.getTag();
                expandPopView();
            }
        });
        addView(tBtn);
        mToggleButtons.add(tBtn);
        RelativeLayout popContainerView = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (mDisplayHeight * 0.6));
        popContainerView.addView(tabItemView, rl);
        popContainerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopView();
            }
        });
        popContainerView.getChildAt(0);
        popContainerView.setBackgroundColor(mPopViewBackgroundColor);
        mViews.add(popContainerView);
    }

    /**
     * setItemData for onelist
     *
     * @param tabPosition
     * @param oneList
     */
    public void setItemData(int tabPosition, List<KeyValue> oneList) {
        if (TYPE_ONE == mTypeList.get(tabPosition)) {
            mOneListMap.get(tabPosition).setData(oneList);
        }
    }

    /**
     * setItemData for twolist
     *
     * @param tabPosition
     * @param parentList
     * @param childList
     * @param parentChildren
     */
    public void setItemData(int tabPosition, List<KeyValue> parentList,
                            List<KeyValue> childList,
                            List<List<KeyValue>> parentChildren) {
        if (TYPE_TWO == mTypeList.get(tabPosition)) {
            mTwoListMap.get(tabPosition).setData(parentList, childList, parentChildren);
        }
    }

    public void refreshItemChildrenData(int tabPosition,
                                        List<KeyValue> childList) {
        if (TYPE_TWO == mTypeList.get(tabPosition)) {
            mTwoListMap.get(tabPosition).setData(null, childList, null);
        }
    }

    /**
     * performClick
     *
     * @param tabPosition
     * @param parentPosition
     * @param childrenPosition
     */
    public void performClick(int tabPosition, int parentPosition, int childrenPosition) {
        if (TYPE_TWO == mTypeList.get(tabPosition)) {
            mTwoListMap.get(tabPosition).clickParent(null, parentPosition);
            mTwoListMap.get(tabPosition).clickChild(null, childrenPosition);
        }
    }

    private void expandPopView() {
        if (mPopupWindow == null) {
            mPopupWindow = new FixedPopupWindow(mViews.get(mSelectPosition), WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(this);
        }
        if (mTbSelected.isChecked()) {
            if (!mPopupWindow.isShowing()) {
                showPopView();
            } else {
                mPopupWindow.dismiss();
            }
        } else {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    }

    private void showPopView() {
        if (mPopupWindow.getContentView() != mViews.get(mSelectPosition)) {
            mPopupWindow.setContentView(mViews.get(mSelectPosition));
        }
        mPopupWindow.showAsDropDown(this);
    }

    private boolean hidePopView() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            if (mTbSelected != null) {
                mTbSelected.setChecked(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDismiss() {
        // TODO: 2017.12.2 刷新默认选择
        if (TYPE_TWO == mTypeList.get(mSelectPosition)) {
            mTwoListMap.get(mSelectPosition).refreshSelected();
        }
    }

    public void setToggleBtnColors(int... colors) {
        mTbtnBackgroundColor = colors[0];
        mTbtnTextColor = colors[1];
    }

    public void setToggleBtnDrawables(int... drawables) {
        mTbtnBackground = drawables[0];
    }

    public ToggleButton getToggleButton(int position) {
        if (mToggleButtons == null) {
            return null;
        } else if (position >= mToggleButtons.size()) {
            return null;
        }
        return mToggleButtons.get(position);
    }


    /**
     * set togglebutton drawables
     *
     * @param tbtn
     */
    public void setToggleButtonDrawable(ToggleButton tbtn) {
        if (mTbtnBackground != -1) {
            tbtn.setBackgroundResource(mTbtnBackground);
        } else {
            tbtn.setBackground(null);
        }
        if (mTbtnTextSize != -1) {
            tbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTbtnTextSize);
        }

        tbtn.setBackgroundColor(mTbtnBackgroundColor);
        tbtn.setTextColor(mTbtnTextColor);
    }

    @Override
    public void unexpandPopView(String title) {
        hidePopView();
        ToggleButton tbtn = (ToggleButton) getChildAt(mSelectPosition);
        tbtn.setTextOff(title);
        tbtn.setText(title);
        tbtn.setTextOn(title);
    }

    public void setAnimationStyle(int style) {
        //set the animation
        mPopupWindow.setAnimationStyle(style);
    }
}
