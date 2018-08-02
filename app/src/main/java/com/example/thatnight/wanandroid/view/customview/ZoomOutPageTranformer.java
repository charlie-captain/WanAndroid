package com.example.thatnight.wanandroid.view.customview;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * date: 2018/8/2
 * author: thatnight
 */
public class ZoomOutPageTranformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
    }
}
