package com.example.thatnight.wanandroid.utils;

import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;

/**
 * date: 2018/7/16
 * author: thatnight
 */
public class DataHelper {

    public static Msg obtainErrorMsg(Object error) {
        return new Msg(Constant.CODE_ERROR, error, null);
    }

    public static Msg obtainMsg(Object data) {
        return new Msg(Constant.CODE_SUCCESS, null, data);
    }

    public static boolean isNullOrEmpty(Msg msg) {
        return msg == null || msg.getErrorCode() != 0;
    }
}
