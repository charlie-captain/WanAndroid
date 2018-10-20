package com.example.thatnight.wanandroid.utils;

import android.widget.Toast;

import com.example.thatnight.wanandroid.base.App;


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
            sToast = Toast.makeText(App.getApplication(), text, Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    /**
     * 只显示一次
     * @param key
     * @param value
     */
    public static void showOnceToast(String key, String value) {
        if (!SharePreferenceUtil.getInstance().getBoolean(key, false)) {
            showToast(value);
            SharePreferenceUtil.getInstance().putBoolean(key, true);
        }
    }

}
