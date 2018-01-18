package com.example.thatnight.wanandroid.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                //检测内存泄漏
                if (LeakCanary.isInAnalyzerProcess(getApplication())) {
                    return;
                }
                LeakCanary.install(getApplication());

                //换肤框架
                SkinCompatManager.withoutActivity(getApplication())
                        .addInflater(new SkinMaterialViewInflater())
                        .addInflater(new SkinConstraintViewInflater())
                        .loadSkin();

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
            }
        }).start();
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        Beta.installTinker(this);
    }


}
