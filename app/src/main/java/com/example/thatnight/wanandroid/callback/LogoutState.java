package com.example.thatnight.wanandroid.callback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

/**
 * Created by ThatNight on 2017.12.18.
 */

public class LogoutState implements UserState {
    @Override
    public boolean collect(final Context context) {
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

        return false;
    }
}
