package com.example.thatnight.wanandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * Created by thatnight on 2017.11.2.
 */

public class ToastUtil {
    private static Toast sToast;
    private static final Handler S_HANDLER = new Handler(Looper.getMainLooper());

    public static void showToast(final Context context, final String text) {
        S_HANDLER.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                if (sToast != null) {
                    sToast.cancel();
                }
                if (sToast != null) {
                    sToast.setText(text);
                } else {
                    sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                }
                sToast.setDuration(Toast.LENGTH_SHORT);
                sToast.show();
            }
        });
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId));
    }

}
