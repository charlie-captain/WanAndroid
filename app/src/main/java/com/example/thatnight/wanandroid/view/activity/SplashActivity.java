package com.example.thatnight.wanandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.model.LoginModel;
import com.example.thatnight.wanandroid.mvp.presenter.LoginPresenter;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = (String) SharePreferenceUtil.get(getApplicationContext(), "account", "");
        String password = (String) SharePreferenceUtil.get(getApplicationContext(), "password", "");

        boolean autologin = (Boolean) SharePreferenceUtil.get(getApplicationContext(), getString(R.string.sp_auto_login), true);
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password) || !autologin) {
            SharePreferenceUtil.put(getApplicationContext(), "visitor", true);
            startActivityAnim(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            LoginContextUtil.getInstance().setUserState(new LoginState());
            SharePreferenceUtil.put(getApplicationContext(), "visitor", false);
            OkHttpCookieJar.initCookies(getApplicationContext());
            startActivityAnim(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    public void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
