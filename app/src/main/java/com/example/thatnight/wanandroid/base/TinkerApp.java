package com.example.thatnight.wanandroid.base;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by ThatNight on 2018.1.18.
 */

public class TinkerApp extends TinkerApplication {

    public TinkerApp() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.example.thatnight.wanandroid.base.App",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
