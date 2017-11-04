package com.example.thatnight.wanandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.thatnight.wanandroid.MainActivity;
import com.example.thatnight.wanandroid.entity.Account;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.model.LoginModel;
import com.example.thatnight.wanandroid.mvp.presenter.LoginPresenter;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String name = (String) SharePreferenceUtil.get(SplashActivity.this, "account", "");
                String password = (String) SharePreferenceUtil.get(SplashActivity.this, "password", "");

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    final boolean isLogin = false;
                    new LoginModel().login(name, password, new LoginPresenter() {
                        @Override
                        public void getResult(Msg msg) {
                            if (msg == null) {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                                return;
                            }
                            Log.d("login", "login: " + msg.getErrorMsg() + "   " + msg.getErrorCode());

                            if (0 == msg.getErrorCode()) {
                                Account account = GsonUtil.gsonToBean(msg.getData().toString(), Account.class);
                                Intent intent = new Intent();
                                intent.setClass(SplashActivity.this, MainActivity.class);
                                intent.putExtra("account", account);
                                startActivity(intent);
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

            }
        }, 1000);
    }
}
