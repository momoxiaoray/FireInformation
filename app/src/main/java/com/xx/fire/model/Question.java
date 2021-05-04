package com.xx.fire.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question extends LitePalSupport implements Serializable {

    private long id;
    private long user_id;//问答发布人id
    private String date;//日期
    private String content;//内容
    private int zan;

    private List<Comment> comment_list = new ArrayList<>();
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public List<Comment> getComment_list() {
        comment_list.clear();
        comment_list.addAll(LitePal.where("question_id = ?", String.valueOf(id)).order("id desc").find(Comment.class, true));
        return comment_list;
    }

    public void setComment_list(List<Comment> comment_list) {
        this.comment_list = comment_list;
    }

    public User getUser() {
        this.user = LitePal.where("id = ?", String.valueOf(user_id)).findFirst(User.class);
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", zan=" + zan +
                ", comment_list=" + comment_list +
                ", user=" + user +
                '}';
    }
}
