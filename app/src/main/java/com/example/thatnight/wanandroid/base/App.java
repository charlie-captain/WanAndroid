package com.example.thatnight.wanandroid.base;

import android.app.Application;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.smtt.sdk.QbSdk;

import cn.bmob.v3.Bmob;
import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by thatnight on 2017.10.25.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //检测内存泄漏
                if(LeakCanary.isInAnalyzerProcess(App.this)){
                    return;
                }
                LeakCanary.install(App.this);

                //换肤框架
                SkinCompatManager.withoutActivity(App.this)
                        .addInflater(new SkinMaterialViewInflater())
                        .addInflater(new SkinConstraintViewInflater())
                        .loadSkin();

                //x5
                QbSdk.initX5Environment(App.this, new QbSdk.PreInitCallback() {
                    @Override
                    public void onCoreInitFinished() {

                    }

                    @Override
                    public void onViewInitFinished(boolean b) {

                    }
                });

                //Bugly
                Bugly.init(getApplicationContext(), getString(R.string.bugly_appkey), false);

                OkHttpUtil.init(getApplicationContext());
                //云后台
                Bmob.initialize(App.this, getString(R.string.bmob_appkey));
            }
        }).start();

    }
}
