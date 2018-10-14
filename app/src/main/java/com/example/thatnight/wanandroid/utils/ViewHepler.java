package com.example.thatnight.wanandroid.utils;

import android.util.EventLog;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.App;
import com.example.thatnight.wanandroid.base.TinkerApp;
import com.example.thatnight.wanandroid.constant.EventBusConfig;
import com.example.thatnight.wanandroid.entity.Msg;

import org.greenrobot.eventbus.EventBus;

/**
 * date: 2018/9/4
 * author: thatnight
 */
public class ViewHepler {

    public static final void saveCommentConfig(boolean isOpen) {
        SharePreferenceUtil.getInstance().putBoolean(App.getApplication().getString(R.string.pref_comment), isOpen);
        EventBus.getDefault().post(isOpen ? EventBusConfig.OPEN_COMMENT : EventBusConfig.CLOSE_COMMENT);
    }

    public static final void saveBannerConfig(boolean isOpen) {
        SharePreferenceUtil.getInstance().putBoolean(App.getApplication().getString(R.string.pref_banner), isOpen);
        EventBus.getDefault().post(isOpen ? EventBusConfig.OPEN_BANNER : EventBusConfig.CLOSE_BANNER);
    }
}
