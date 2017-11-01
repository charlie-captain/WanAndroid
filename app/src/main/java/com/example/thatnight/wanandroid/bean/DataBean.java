package com.example.thatnight.wanandroid.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class DataBean implements Parcelable {
    /**
     * id : 624
     * username : thatnight
     * password : 703692499
     * icon : null
     * type : 0
     * collectIds : []
     */

    private int id;
    private String username;
    private String password;
    private Object icon;
    private int type;
    private List<Integer> collectIds;

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

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeParcelable((Parcelable) this.icon, flags);
        dest.writeInt(this.type);
        dest.writeList(this.collectIds);
    }

    public DataBean() {
    }

    protected DataBean(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.icon = in.readParcelable(Object.class.getClassLoader());
        this.type = in.readInt();
        this.collectIds = new ArrayList<Integer>();
        in.readList(this.collectIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel source) {
            return new DataBean(source);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };
}
