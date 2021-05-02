package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Comment extends LitePalSupport implements Serializable {

    private String comment_username;//评论人
    private int style;//0评论，1回复
    private String comment_content;//评论内容
    private String recover_user;//评论的某人
    private String recover_content;//评论的原内容
    private String date;

    public String getComment_username() {
        return comment_username;
    }

    public void setComment_username(String comment_username) {
        this.comment_username = comment_username;
    }

    public String getRecover_content() {
        return recover_content;
    }

    public void setRecover_content(String recover_content) {
        this.recover_content = recover_content;
    }

    public String getRecover_user() {
        return recover_user;
    }

    public void setRecover_user(String recover_user) {
        this.recover_user = recover_user;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_username='" + comment_username + '\'' +
                ", style=" + style +
                ", comment_content='" + comment_content + '\'' +
                ", recover_user='" + recover_user + '\'' +
                ", recover_content='" + recover_content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
