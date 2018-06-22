package com.example.thatnight.wanandroid.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.okhttp3.Cookie;
import cn.bmob.v3.okhttp3.CookieJar;
import cn.bmob.v3.okhttp3.HttpUrl;

public class OkHttpCookieJar implements CookieJar {
//    private static List<Cookie> sCookies;
//    private final PersistentCookieStore cookieStore;

    public OkHttpCookieJar(Context mContext) {
//        cookieStore = new PersistentCookieStore(mContext);
    }

    //    /**
//     * 自动管理Cookies
//     */
//    @Override
//    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        if (cookies != null && cookies.size() > 0) {
//            for (Cookie item : cookies) {
//                cookieStore.add(url, item);
//            }
//        }
//    }
//
//    @Override
//    public List<Cookie> loadForRequest(HttpUrl url) {
//        List<Cookie> cookies = cookieStore.get(url);
//        return cookies;
//    }
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

//    @Override
//    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        if (cookies != null && cookies.size() > 0) {
//            if (sCookies == null) {
//                sCookies = cookies;
//            } else {
//                sCookies.addAll(cookies);
//            }
//        }
//    }
//
//    @Override
//    public List<Cookie> loadForRequest(HttpUrl url) {
//        if (null != sCookies) {
//            return sCookies;
//        } else {
//            return new ArrayList<>();
//        }
//    }

//    public static void printCookie() {
//        if (sCookies != null) {
//
//            for (int i = 0; i < sCookies.size(); i++) {
//                Log.e("cc", sCookies.get(i).value());
//            }
//        }
//    }

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

    public static void saveCookies(Context context) {
        if (!OkHttpCookieJar.isCookiesNull()) {
            SharePreferenceUtil.put(context, "cookies",
                    GsonUtil.gsonToJson(OkHttpCookieJar.getWanAndroidCookies()));
        }
    }

    public static void initCookies(Context context) {
        String cookies = (String) SharePreferenceUtil.get(context, "cookies", "");

        if (!TextUtils.isEmpty(cookies)) {
            List<Cookie> list = GsonUtil.gsonToList(cookies, Cookie.class);
            cookieStore.put("www.wanandroid.com", list);
        }
    }

}
