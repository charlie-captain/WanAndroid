package com.example.thatnight.wanandroid.callback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.utils.ViewHepler;
import com.example.thatnight.wanandroid.view.activity.LoginActivity;

/**
 * Created by ThatNight on 2017.12.18.
 */

public class LogoutState implements UserState {
    @Override
    public boolean collect(final Context context) {
        ViewHepler.showLoginDialog(context);
        return false;
    }
}
