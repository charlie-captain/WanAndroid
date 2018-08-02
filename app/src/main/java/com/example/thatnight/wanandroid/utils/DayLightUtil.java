package com.example.thatnight.wanandroid.utils;

import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.TinkerApp;

import java.util.Calendar;

import skin.support.SkinCompatManager;

/**
 * date: 2018/8/2
 * author: thatnight
 */
public class DayLightUtil {

    public static void autoDayLight() {
        boolean isAutoDayLight = SharePreferenceUtil.getInstance().getBoolean(TinkerApp.getApplication().getString(R.string.pref_auto_day_light), false);
        boolean isDayLight = false;
        if (isAutoDayLight) {
            String time = SharePreferenceUtil.getInstance().optString(TinkerApp.getApplication().getString(R.string.summary_auto_day_light));
            String[] times = time.split("~");
            int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
            int endHour = Integer.parseInt(times[1].split(":")[0].trim());
            int endMinute = Integer.parseInt(times[1].split(":")[1].trim());
            int startHour = Integer.parseInt(times[0].split(":")[0].trim());
            int startMinute = Integer.parseInt(times[0].split(":")[1].trim());

            if (startHour < nowHour && nowHour < endHour) {
                isDayLight = true;
            }
            if (startHour == nowHour) {
                if (nowMinute > startMinute ) {
                    isDayLight = true;
                } else {
                    isDayLight = false;
                }
            }
            if (nowHour == endHour) {
                if (nowMinute <= endMinute) {
                    isDayLight = true;
                } else {
                    isDayLight = false;
                }
            }
        } else {
            isDayLight = false;
        }
        if (isDayLight) {
            setDayLight();
        } else {
            revertSkin();
        }
    }

    public static void setDayLight() {
        SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
    }

    public static void revertSkin() {
        String skin = SharePreferenceUtil.getInstance().optString("skin");
        if (TextUtils.isEmpty(skin)) {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        } else if ("green".equals(skin)) {
            SkinCompatManager.getInstance().loadSkin("green", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
        } else if ("blue".equals(skin)) {

        }
    }
}
