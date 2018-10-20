package com.example.thatnight.wanandroid.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.thatnight.wanandroid.view.activity.MainActivity;

/**
 * 退出工具类
 * Created by ThatNight on 2017.12.23.
 */

public class ExitUtil {
    private static long mFirstTime = 0;

    public static void exitCheck(Activity activity,View view) {
        if (System.currentTimeMillis() - mFirstTime > 2000) {
           Snackbar.make(view, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mFirstTime = System.currentTimeMillis();
        } else {
            activity.finish();
            System.exit(0);
        }
    }

}
