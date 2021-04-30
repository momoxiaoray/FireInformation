package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class News extends LitePalSupport {

    private String title;
    private String content;
    private String date;
    private int scan_count;
    private int news_type = 0;

    //一条新闻可以有多个用户收藏操作
    private List<User> users;


    public List<User> getUsers() {
        return users == null ? new ArrayList<>() : users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getNews_type() {
        return news_type;
    }

    public void setNews_type(int news_type) {
        this.news_type = news_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScan_count() {
        return scan_count;
    }

    public void setScan_count(int scan_count) {
        this.scan_count = scan_count;
    }
}
