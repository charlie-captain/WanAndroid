package com.example.thatnight.wanandroid.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.thatnight.wanandroid.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.lang.reflect.Type;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by ThatNight on 2017.12.20.
 */

public class SkinRefreshLayout extends SmartRefreshLayout implements SkinCompatSupportable {


    protected int mAccentColor;
    protected int mPrimaryColor;

    public SkinRefreshLayout(Context context) {
        this(context, null);
    }

    public SkinRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRefreshLayout, defStyleAttr, 0);
        mAccentColor = ta.getResourceId(R.styleable.SmartRefreshLayout_srlAccentColor, INVALID_ID);
        mPrimaryColor = ta.getResourceId(R.styleable.SmartRefreshLayout_srlPrimaryColor, INVALID_ID);

        ta.recycle();
        setColor();
    }

    private void setColor() {
        if (mPrimaryColor != 0) {
            int primaryColor = SkinCompatResources.getInstance().getColor(mPrimaryColor);
            if (mPrimaryColor != 0) {
                int accentColor = SkinCompatResources.getInstance().getColor(mAccentColor);
                mPrimaryColors = new int[]{primaryColor, accentColor};
            } else {
                mPrimaryColors = new int[]{primaryColor};
            }
            setPrimaryColors(mPrimaryColors);
        }
    }


    @Override
    public void applySkin() {
        setColor();
    }
}
