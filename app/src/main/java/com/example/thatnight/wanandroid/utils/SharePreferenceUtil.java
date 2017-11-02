package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thatnight on 2017.11.2.
 */

public class SharePreferenceUtil {

    private static SharedPreferences sSp;
    private static SharedPreferences.Editor sEditor;

    public static final String FILE_NAME = "wanandroid";


    public static void put(Context context, String key, Object o) {
        put(context, FILE_NAME, key, o);
    }

    public static void put(Context context, String spName, String key, Object o) {
        initSp(context, spName);
        initEditor();
        if (o instanceof String) {
            sEditor.putString(key, (String) o);
        } else if (o instanceof Integer) {
            sEditor.putInt(key, (Integer) o);
        } else if (o instanceof Boolean) {
            sEditor.putBoolean(key, (Boolean) o);
        } else if (o instanceof Float) {
            sEditor.putFloat(key, (Float) o);
        } else if (o instanceof Long) {
            sEditor.putLong(key, (Long) o);
        }
        sEditor.apply();
    }

    private static void initEditor() {
        sEditor = sSp.edit();
    }

    private static void initSp(Context context, String spName) {
        if (sSp == null) {
            sSp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
    }

    public static Object get(Context context, String key, Object o) {
        return get(context, FILE_NAME, key, o);
    }

    public static Object get(Context context, String spName, String key, Object o) {
        initSp(context, spName);
        if (o instanceof String) {
            return sSp.getString(key, (String) o);
        } else if (o instanceof Integer) {
            return sSp.getInt(key, (Integer) o);
        } else if (o instanceof Boolean) {
            return sSp.getBoolean(key, (Boolean) o);
        } else if (o instanceof Float) {
            return sSp.getFloat(key, (Float) o);
        } else if (o instanceof Long) {
            return sSp.getLong(key, (Long) o);
        }
        return null;
    }

    public static void remove(Context context, String key) {
        remove(context, FILE_NAME, key);
    }

    public static void remove(Context context, String spName, String key) {
        initSp(context, spName);
        initEditor();
        sEditor.remove(key);
        sEditor.apply();
    }

    public static void clear(Context context) {
        clear(context, FILE_NAME);
    }

    private static void clear(Context context, String fileName) {
        initSp(context, fileName);
        initEditor();
        sEditor.clear();
        sEditor.apply();
    }
}
