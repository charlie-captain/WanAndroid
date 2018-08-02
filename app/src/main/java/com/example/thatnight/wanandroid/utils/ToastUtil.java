package com.example.thatnight.wanandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.thatnight.wanandroid.base.TinkerApp;


/**
 * Created by thatnight on 2017.11.2.
 */

public class ToastUtil {
    private static Toast sToast;

    public static void showToast(final String text) {
        if (sToast != null) {
            sToast.setText(text);
            sToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            sToast = Toast.makeText(TinkerApp.getApplication(), text, Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

}
