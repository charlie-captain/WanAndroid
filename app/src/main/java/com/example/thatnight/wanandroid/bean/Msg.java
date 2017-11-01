package com.example.thatnight.wanandroid.bean;

/**
 * Created by thatnight on 2017.11.1.
 */

public class Msg {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : {"id":624,"username":"thatnight","password":"703692499","icon":null,"type":0,"collectIds":[]}
     */

    private int errorCode;
    private Object errorMsg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


}
