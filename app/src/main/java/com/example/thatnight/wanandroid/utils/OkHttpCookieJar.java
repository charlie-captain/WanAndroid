package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public class OkHttpCookieJar implements CookieJar {

    public OkHttpCookieJar(Context mContext) {
    }


    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }


    public static boolean isCookiesNull() {
        if (cookieStore == null) {
            return true;
        }
        return false;
    }

    public static void resetCookies() {
        cookieStore = new HashMap<>();
    }

    public static List<Cookie> getWanAndroidCookies() {
        return cookieStore.get("www.wanandroid.com");
    }

    public static void saveCookies() {
        if (!OkHttpCookieJar.isCookiesNull()) {
            SharePreferenceUtil.getInstance().putString("cookies",
                    GsonUtil.gsonToJson(OkHttpCookieJar.getWanAndroidCookies()));
        }
    }

    public static void initCookies() {
        String cookies =  SharePreferenceUtil.getInstance().optString("cookies");
        if (!TextUtils.isEmpty(cookies)) {
            List<Cookie> list = GsonUtil.gsonToList(cookies, Cookie.class);
            cookieStore.put("www.wanandroid.com", list);
        }
    }

}
