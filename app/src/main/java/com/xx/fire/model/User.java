package com.xx.fire.model;

import androidx.annotation.NonNull;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends LitePalSupport implements Serializable {
    private long id;
    private String account;
    private String nickname;
    private String password;
    private boolean ismanager = false;

    //一个用户有多个新闻收藏
    private List<News> news_list = new ArrayList<>();
    //一个用户可以多个动态
    private List<Dynamic> dynamics = new ArrayList<>();

    //一个用户多个评论
    private List<Comment> comments = new ArrayList<>();

    private List<QuestionAnswer> answers =new ArrayList<>();

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Dynamic> getDynamics() {
        return dynamics;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDynamics(List<Dynamic> dynamics) {
        this.dynamics = dynamics;
    }

    public List<News> getNews_list() {
        return news_list;
    }

    public void setNews_list(List<News> news_list) {
        this.news_list = news_list;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsmanager() {
        return ismanager;
    }

    public void setIsmanager(boolean ismanager) {
        this.ismanager = ismanager;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        boolean equals = Objects.equals(account, user.account) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(password, user.password);
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, nickname, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", ismanager=" + ismanager +
                ", news_list=" + news_list +
                '}';
    }
}
