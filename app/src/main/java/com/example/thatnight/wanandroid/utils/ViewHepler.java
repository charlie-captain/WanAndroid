package com.example.thatnight.wanandroid.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.App;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.constant.EventBusConfig;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * date: 2018/9/4
 * author: thatnight
 */
public class ViewHepler {

    public static final void saveCommentConfig(boolean isOpen) {
        SharePreferenceUtil.getInstance().putBoolean(App.getApplication().getString(R.string.pref_comment), isOpen);
        EventBus.getDefault().post(isOpen ? EventBusConfig.OPEN_COMMENT : EventBusConfig.CLOSE_COMMENT);
    }

    public static final void saveBannerConfig(boolean isOpen) {
        SharePreferenceUtil.getInstance().putBoolean(App.getApplication().getString(R.string.pref_banner), isOpen);
        EventBus.getDefault().post(isOpen ? EventBusConfig.OPEN_BANNER : EventBusConfig.CLOSE_BANNER);
    }

    /**
     * 显示登录弹窗
     * @param context
     */
    public static final void showLoginDialog(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("请先登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).startActivityForResult(new Intent(context, LoginActivity.class), Constant.REQUEST_LOGIN);
                        ((Activity) context).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
