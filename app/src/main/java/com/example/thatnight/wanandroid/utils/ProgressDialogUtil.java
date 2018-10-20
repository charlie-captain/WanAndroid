package com.example.thatnight.wanandroid.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by thatnight on 2017.11.1.
 */

public class ProgressDialogUtil {
    public static final int GRAVITY_TOP = 0;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_BOTTOM = 2;
    private static ProgressDialog sProgressDialog;
    private static boolean isShow = false;
    private static Context mContext;

    public static void show(Context context, CharSequence msg) {
        if (sProgressDialog == null) {
            init(context);
        }
        if (((Activity) mContext).isFinishing()) {
            init(context);
        }
        if (sProgressDialog.isShowing()) {
            return;
        }
        try {
            sProgressDialog.setMessage(msg);
            sProgressDialog.setCancelable(true);
            sProgressDialog.show();
            isShow = true;
        } catch (Exception e) {
            e.printStackTrace();
            sProgressDialog=null;
        }
    }

    private static void init(Context context) {
        sProgressDialog = new ProgressDialog(context);
        mContext = context;
    }

    public static void show(Context context) {
        show(context, "正在加载...");
    }

    public static void setMsg(CharSequence msg) {
        if (sProgressDialog != null) {
            sProgressDialog.setMessage(msg);
        }
    }

    public static void setGravity(int gravity) {
        Window window = sProgressDialog.getWindow();
        switch (gravity) {
            case GRAVITY_TOP:
                window.setGravity(Gravity.TOP);
                break;
            case GRAVITY_CENTER:
                window.setGravity(Gravity.CENTER);
                break;
            case GRAVITY_BOTTOM:
                window.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.y = 400;
                window.setAttributes(layoutParams);
                break;
            default:
                window.setGravity(Gravity.CENTER);
                break;
        }

    }

    public static void dismiss() {
        if (sProgressDialog != null && sProgressDialog.isShowing()) {
            sProgressDialog.dismiss();
            sProgressDialog = null;
            isShow = false;
        }
    }

    public static boolean isShow() {
        return isShow;
    }

}
