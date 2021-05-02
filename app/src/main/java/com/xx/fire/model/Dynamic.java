package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dynamic extends LitePalSupport implements Serializable {

    private User user;//动态发布人
    private String date;//日期
    private String dynamic_content;//内容
    private List<MediaData> media_list = new ArrayList<>();//媒体，图片或者视频
    private int zan;
    private List<Comment> comment_list = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDynamic_content() {
        return dynamic_content;
    }

    public void setDynamic_content(String dynamic_content) {
        this.dynamic_content = dynamic_content;
    }

    public List<MediaData> getMedia_list() {
        return media_list;
    }

    public void setMedia_list(List<MediaData> media_list) {
        this.media_list = media_list;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public List<Comment> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Comment> comment_list) {
        this.comment_list = comment_list;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "user=" + user +
                ", date='" + date + '\'' +
                ", dynamic_content='" + dynamic_content + '\'' +
                ", media_list=" + media_list +
                ", zan=" + zan +
                ", comment_list=" + comment_list +
                '}';
    }
}
