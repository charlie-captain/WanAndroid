package com.example.thatnight.wanandroid.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.v3.Bmob;
import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by thatnight on 2017.10.25.
 */

public class App extends DefaultApplicationLike {

    public App(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //右滑返回
        BGASwipeBackHelper.init(getApplication(), null);

        //检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(getApplication())) {
            return;
        }
        LeakCanary.install(getApplication());

        //换肤框架
        SkinCompatManager.withoutActivity(getApplication()).addInflater(new SkinMaterialViewInflater()).addInflater(new SkinConstraintViewInflater()).loadSkin();

        //x5
        QbSdk.initX5Environment(getApplication(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });

        //Bugly
        Bugly.init(getApplication(), getApplication().getString(R.string.bugly_appkey), false);

        OkHttpUtil.init(getApplication());
        //云后台
        Bmob.initialize(getApplication(), getApplication().getString(R.string.bmob_appkey));
        //cookie
        OkHttpCookieJar.initCookies();
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        Beta.installTinker(this);
    }




}
