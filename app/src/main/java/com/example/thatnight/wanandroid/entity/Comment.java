package com.example.thatnight.wanandroid.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by ThatNight on 2018.1.7.
 */

public class Comment extends BmobObject {

    private String mUserName;       //用户名
    private String mDeviceId;       //设备标识符
    private String mContent;        //内容
    private String mVersion;        //app版本号
    private String mPhoneName;      //手机型号
    private String mPhoneVersion;   //手机系统版本

    public String getPhoneVersion() {
        return mPhoneVersion;
    }

    public void setPhoneVersion(String phoneVersion) {
        mPhoneVersion = phoneVersion;
    }

    public Comment() {

    }

    public String getPhoneName() {
        return mPhoneName;
    }

    public void setPhoneName(String phoneName) {
        mPhoneName = phoneName;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }


}
