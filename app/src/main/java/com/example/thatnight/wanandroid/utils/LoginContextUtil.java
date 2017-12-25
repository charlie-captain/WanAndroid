package com.example.thatnight.wanandroid.utils;

import android.content.Context;

import com.example.thatnight.wanandroid.callback.LogoutState;
import com.example.thatnight.wanandroid.callback.UserState;

import java.lang.ref.WeakReference;

/**
 * Created by ThatNight on 2017.12.18.
 */

public class LoginContextUtil {

    UserState mUserState = new LogoutState();

    private boolean mHasContext;

    public LoginContextUtil() {
        mHasContext = false;
    }

    public static LoginContextUtil getInstance() {
        return Holder.sInstance;
    }

    public UserState getUserState() {
        return mUserState;
    }

    public void setUserState(UserState userState) {
        mUserState = userState;
    }

    static class Holder {
        private static final LoginContextUtil sInstance = new LoginContextUtil();
    }

    void collect(Context context) {
        mUserState.collect(context);
        if (context != null) {
            mHasContext = true;
        }
    }

    public boolean hasContext() {
        return mHasContext;
    }

}
