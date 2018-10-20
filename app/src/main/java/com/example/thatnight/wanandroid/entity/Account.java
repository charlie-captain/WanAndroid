package com.example.thatnight.wanandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.2.
 */

public class Account implements Parcelable {


    /**
     * id : 624
     * username : thatnight
     * password : xxxxxxxxx
     * icon : null
     * type : 0
     * collectIds : []
     * email :
     * icon :
     */

    private int id;
    private String username;
    private String password;
    private int type;
    private List<Integer> collectIds;
    private String email;
    @SerializedName("icon")
    private String iconX;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<?> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconX() {
        return iconX;
    }

    public void setIconX(String iconX) {
        this.iconX = iconX;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeInt(this.type);
        dest.writeList(this.collectIds);
        dest.writeString(this.email);
        dest.writeString(this.iconX);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.type = in.readInt();
        this.collectIds = new ArrayList<Integer>();
        in.readList(this.collectIds, Integer.class.getClassLoader());
        this.email = in.readString();
        this.iconX = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
