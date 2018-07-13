package com.example.thatnight.wanandroid.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * date: 2018/7/11
 * author: thatnight
 */
public class BmobAccount extends BmobUser {

    private BmobFile icon;

    private int id;

    private String nickName;

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
