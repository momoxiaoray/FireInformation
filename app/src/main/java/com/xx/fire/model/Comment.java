package com.xx.fire.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class Comment extends LitePalSupport implements Serializable {
    private long id;
    private long user_id;//评论人id,用于查询
    private long dynamic_id;//动态id,用于查询
    private int style;//0评论，1回复
    private String comment_content;//评论内容
    private String recover_user;//评论的某人
    private String recover_content;//评论的原内容
    private String date;

    //一个评论对应一个动态
    private Dynamic dynamic;
    //一个评论对应一个用户
    private User user;

    public long getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(long dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Dynamic getDynamic() {
        this.dynamic = LitePal.where("id = ?", String.valueOf(dynamic_id)).findFirst(Dynamic.class);
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public User getUser() {
        this.user = LitePal.where("id = ?", String.valueOf(user_id)).findFirst(User.class);
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
                "id=" + id +
                ", user_id=" + user_id +
                ", style=" + style +
                ", comment_content='" + comment_content + '\'' +
                ", recover_user='" + recover_user + '\'' +
                ", recover_content='" + recover_content + '\'' +
                ", date='" + date + '\'' +
                ", dynamic=" + dynamic +
                ", user=" + user +
                '}';
    }
}
