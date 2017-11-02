package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


/**
 * Created by thatnight on 2017.11.2.
 */

public class ToastUtil {
    private static Toast sToast;
    private static Handler sHandler;
    private static Runnable sRunnable;

    public static void showToast(Context context, String text) {
        if (sHandler == null) {
            sHandler = new Handler();
            sRunnable = new Runnable() {
                @Override
                public void run() {
                    sToast.cancel();
                }
            };
        }
        sHandler.removeCallbacks(sRunnable);
        if (sToast != null) {
            sToast.setText(text);
        } else {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        sHandler.postDelayed(sRunnable,4000);
        sToast.show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId));
    }

}
