package com.example.thatnight.wanandroid.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatViewInflater;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.utils.OkHttpCookieJar;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.smtt.sdk.QbSdk;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bmob.v3.Bmob;
import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by thatnight on 2017.10.25.
 */
public class App extends MultiDexApplication {


    private static App sTinkerApp;

    @Override
    public void onCreate() {
        super.onCreate();
//        TinkerManager.installTinker(this);
        //右滑返回
        BGASwipeBackHelper.init(this, null);

        //检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        //换肤框架
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .setSkinStatusBarColorEnable(true)
                .setSkinAllActivityEnable(true)
                .setSkinWindowBackgroundEnable(true)
                .loadSkin();

        //x5
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });

        //Bugly
        Bugly.init(this, this.getString(R.string.bugly_appkey), false);

        OkHttpUtil.init(this);
        //云后台
        Bmob.initialize(this, this.getString(R.string.bmob_appkey));
        //cookie
        OkHttpCookieJar.initCookies();


    }

//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        Beta.installTinker(this);
//    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sTinkerApp = this;
    }

    public static Application getApplication() {
        if (sTinkerApp == null) {
            throw new NullPointerException("appcontext not create or destroy");
        }
        return sTinkerApp;
    }
}
