package com.example.thatnight.wanandroid.base;

import android.app.Application;
import android.content.Context;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by ThatNight on 2018.1.18.
 */

public class TinkerApp extends TinkerApplication {

    private static TinkerApp sTinkerApp;

    public TinkerApp() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.example.thatnight.wanandroid.base.App",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sTinkerApp= this;
    }

    public static Application getApplication(){
        if(sTinkerApp==null){
            throw new NullPointerException("appcontext not create or destroy");
        }
        return sTinkerApp;
    }


}
