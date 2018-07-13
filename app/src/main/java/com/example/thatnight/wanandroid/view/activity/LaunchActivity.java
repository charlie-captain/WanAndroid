package com.example.thatnight.wanandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.callback.LoginState;
import com.example.thatnight.wanandroid.utils.AccountUtil;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;

public class LaunchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = (String) SharePreferenceUtil.getInstance().getString("account", "");
        String password = (String) SharePreferenceUtil.getInstance().getString("password", "");

        boolean autologin = (Boolean) SharePreferenceUtil.getInstance().getBoolean(getString(R.string.sp_auto_login), true);
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password) || !autologin) {
            SharePreferenceUtil.getInstance().putBoolean("visitor", true);
            startActivityAnim(MainActivity.class);
        } else {
            if (AccountUtil.getAccount() == null) {
                //初始化bmob账号
                startActivityAnim(LoginActivity.class);
                return;
            }
            LoginContextUtil.getInstance().setUserState(new LoginState());
            SharePreferenceUtil.getInstance().putBoolean("visitor", false);
            startActivityAnim(MainActivity.class);
        }

        finish();
    }

    public void startActivityAnim(Class cl) {
        Intent intent = new Intent(LaunchActivity.this, cl);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
