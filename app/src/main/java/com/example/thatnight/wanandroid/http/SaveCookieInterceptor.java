package com.example.thatnight.wanandroid.http;

import android.text.TextUtils;

import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SaveCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (!response.headers("set-cookie").isEmpty()) {
            List<String> cookies = response.headers("set-cookie");
            String cookie = encodeCookie(cookies);
            saveCookies(request.url().toString(), request.url().host(), cookie);
        }
        return response;
    }

    private void saveCookies(String s, String host, String cookie) {
        if (TextUtils.isEmpty(s)) {
            throw new NullPointerException("url is numm");
        } else {
            SharePreferenceUtil.getInstance().putString(s, cookie);
        }
        if (!TextUtils.isEmpty(host)) {
            SharePreferenceUtil.getInstance().putString(host, cookie);
        }
    }


    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) {
                    continue;
                }
                set.add(s);
            }
        }
        for (String cookie : set) {
            sb.append(cookie).append(";");
        }
        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }
        return sb.toString();
    }


    public static void clearCookie() {
        SharePreferenceUtil.getInstance().remove("www.wanandroid.com");
    }

}
