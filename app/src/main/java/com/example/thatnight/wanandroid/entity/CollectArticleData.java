package com.example.thatnight.wanandroid.entity;

import java.util.List;

/**
 * Created by thatnight on 2017.11.2.
 */

public class CollectArticleData {


    /**
     * datas : [{"id":1485,"title":"Android中图片压缩分析（上）","chapterId":86,"chapterName":"图片处理","envelopePic":null,"link":"https://mp.weixin.qq.com/s/QZ-XTsO7WnNvpnbr3DWQmg","author":"QQ音乐技术团队","origin":null,"publishTime":1509611122000,"zan":0,"desc":null,"visible":0,"niceDate":"2小时前","courseId":13,"onCollect":false}]
     * offset : 0
     * size : 20
     * total : 771
     * over : false
     */

    private int offset;
    private int size;
    private int total;
    private boolean over;
    private List<CollectArticle> datas;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public List<CollectArticle> getDatas() {
        return datas;
    }

    public void setDatas(List<CollectArticle> datas) {
        this.datas = datas;
    }

}
