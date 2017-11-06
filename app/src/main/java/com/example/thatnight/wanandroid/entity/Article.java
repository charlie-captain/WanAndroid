package com.example.thatnight.wanandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thatnight on 2017.11.3.
 */

public class Article extends CollectArticle implements Parcelable {
    /**
     * id : 1485
     * title : Android中图片压缩分析（上）
     * chapterId : 86
     * chapterName : 图片处理
     * envelopePic : null
     * link : https://mp.weixin.qq.com/s/QZ-XTsO7WnNvpnbr3DWQmg
     * author : QQ音乐技术团队
     * origin : null
     * publishTime : 1509611122000
     * zan : 0
     * desc : null
     * visible : 0
     * niceDate : 2小时前
     * courseId : 13
     * collect : false
     */

    private boolean collect;

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.collect ? (byte) 1 : (byte) 0);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.collect = in.readByte() != 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
