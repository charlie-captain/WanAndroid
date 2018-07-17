package com.example.thatnight.wanandroid.entity;

import com.example.thatnight.wanandroid.constant.Constant;

/**
 * Created by thatnight on 2017.11.2.
 */

public class Msg {


    /**
     * errorCode : 0
     * errorMsg : null
     * data : jj
     */

    private int errorCode;
    private Object errorMsg;
    private Object data;
    private Object data2;

    public Msg() {
    }

    public Msg(int errorCode, Object errorMsg, Object data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData2() {
        return data2;
    }

    public void setData2(Object data2) {
        this.data2 = data2;
    }
}
