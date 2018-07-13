package com.example.thatnight.wanandroid.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * date: 2018/7/11
 * author: thatnight
 */
public class ArticleComment extends BmobObject {


    private int articleId;

    private BmobAccount account;

    private BmobAccount toAccount;

    private String content;

    private BmobFile file;

    private BmobRelation likes;

    private ArticleComment parentComment;

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public ArticleComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ArticleComment parentComment) {
        this.parentComment = parentComment;

    }

    public BmobAccount getAccount() {
        return account;
    }

    public void setAccount(BmobAccount account) {
        this.account = account;
    }

    public BmobAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(BmobAccount toAccount) {
        this.toAccount = toAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

}
