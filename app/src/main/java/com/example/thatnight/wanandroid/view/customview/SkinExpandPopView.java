package com.example.thatnight.wanandroid.view.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.example.expandpopview.view.ExpandPopView;
import com.example.thatnight.wanandroid.R;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * 自定义换肤第三方列表
 * Created by ThatNight on 2017/12/8.
 */

public class SkinExpandPopView extends ExpandPopView implements SkinCompatSupportable {

    private int mTbtnBackgroundColor;       //togglebutton bgcolor
    private int mTbtnTextColor;             //togglebutton textcolor
    private int mPopViewTextColor;          //popview text color

    public SkinExpandPopView(Context context) {
        this(context, null);
    }

    public SkinExpandPopView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinExpandPopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandPopView, defStyleAttr, 0);
        mTbtnBackgroundColor = a.getResourceId(R.styleable.ExpandPopView_tab_togglebtn_bg_color, INVALID_ID);
        mTbtnTextColor = a.getResourceId(R.styleable.ExpandPopView_tab_togglebtn_text_color, INVALID_ID);
        mPopViewTextColor = a.getResourceId(R.styleable.ExpandPopView_tab_pop_text_color, INVALID_ID);
        a.recycle();
        setColor();
    }

    private void setColor() {
        int backgroundColor = 0, textColor = 0, popViewTextColor = 0;
        if (mTbtnBackgroundColor != INVALID_ID) {
            backgroundColor = SkinCompatResources.getInstance().getColor(mTbtnBackgroundColor);
        }
        if (mTbtnTextColor != INVALID_ID) {
            textColor = SkinCompatResources.getInstance().getColor(mTbtnTextColor);
        }
        if (mPopViewTextColor != INVALID_ID) {
            popViewTextColor = SkinCompatResources.getInstance().getColor(mPopViewTextColor);
        }
        setToggleBtnColors(backgroundColor, textColor);

        if (mToggleButtons != null && mToggleButtons.size() > 0) {
            for (int i = 0; i < mToggleButtons.size(); i++) {
                if (getToggleButton(i) != null) {
                    setToggleButtonDrawable(getToggleButton(i));
                }
            }
        }
//        if (getToggleButton(0) != null) {
//            setToggleButtonDrawable(getToggleButton(0));
//        }
    }


    @Override
    public void applySkin() {
        setColor();
    }


}
