package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.thatnight.wanandroid.base.TinkerApp;

/**
 * Created by thatnight on 2017.11.2.
 */

public class SharePreferenceUtil {

    private static SharedPreferences sSp;
    private static SharedPreferences.Editor sEditor;

    public static final String FILE_NAME = "wanandroid";

    public static SharePreferenceUtil getInstance() {
        return InstanceHolder.sInstance;
    }

    public SharePreferenceUtil() {
        sSp = TinkerApp.getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sEditor = sSp.edit();
    }

    static class InstanceHolder {
        private static final SharePreferenceUtil sInstance = new SharePreferenceUtil();
    }


    public int getInt(String key, int value) {
        return sSp.getInt(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return sSp.getBoolean(key, value);
    }

    public String optString(String key) {
        return getString(key, "");
    }

    public Long optLong(String key) {
        return getLong(key, 0);
    }

    public Boolean optBoolean(String key) {
        return getBoolean(key, false);
    }

    public int optInt(String key) {
        return getInt(key, 0);
    }

    public String getString(String key, String value) {
        return sSp.getString(key, value);
    }

    public long getLong(String key, long value) {
        return sSp.getLong(key, value);
    }


    public boolean putInt(String key, int params) {
        try {
            sEditor.putInt(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putString(String key, String params) {
        try {
            sEditor.putString(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;

    }

    public boolean putBoolean(String key, boolean params) {
        try {
            sEditor.putBoolean(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putLong(String key, long params) {
        try {
            sEditor.putLong(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public void remove(String key) {
        remove(FILE_NAME, key);
    }

    public void remove(String spName, String key) {
        sEditor.remove(key);
        sEditor.apply();
    }

    public void clear() {
        clear(FILE_NAME);
    }

    private void clear(String fileName) {
        sEditor.clear();
        sEditor.apply();
    }
}
