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
//        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        String name = (String) SharePreferenceUtil.get(getApplicationContext(), "account", "");
        String password = (String) SharePreferenceUtil.get(getApplicationContext(), "password", "");

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password)) {
            SharePreferenceUtil.put(getApplicationContext(), "visitor", true);
            startActivityAnim(new Intent(SplashActivity.this, MainActivity.class));
        } else {
//            new LoginModel().login(name, password, new LoginPresenter() {
//                @Override
//                public void getResult(Msg msg) {
//                    if (msg == null) {
//                        ToastUtil.showToast(getApplicationContext(), "网络出了点问题,登录失败");
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        finish();
//                        return;
//                    }
//                    Log.d("login", "login: " + msg.getErrorMsg() + "   " + msg.getErrorCode());
//
//                    if (0 == msg.getErrorCode()) {
            LoginContextUtil.getInstance().setUserState(new LoginState());
            SharePreferenceUtil.put(getApplicationContext(), "visitor", false);
            OkHttpCookieJar.initCookies(getApplicationContext());
//                        Account account = GsonUtil.gsonToBean(msg.getData().toString(), Account.class);
//            Account account = new Account();
//            account.setUsername(name);
//            Intent intent = new Intent();
//            intent.setClass(SplashActivity.this, MainActivity.class);
//            intent.putExtra("account", account);
//            startActivityAnim(intent);
//                        finish();
//                    } else {
//                        ToastUtil.showToast(getApplicationContext(), "服务器开小差了,登录失败");
            startActivityAnim(new Intent(SplashActivity.this, MainActivity.class));
//                    }
//                }
//            });
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();


    }

    public void startActivityAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
