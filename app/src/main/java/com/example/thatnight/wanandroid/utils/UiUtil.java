package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by thatnight on 2017.10.31.
 */

public class UiUtil {

    public static void setSelected(View v) {
        v.setSelected(!v.isSelected());
    }

    public static void inputSoftWare(boolean isShow, View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
