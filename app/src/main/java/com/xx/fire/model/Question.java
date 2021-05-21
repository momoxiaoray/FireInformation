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
    private List<QuestionAnswer> answer = new ArrayList<>();
    private long right_answer_id;
    private String right_answer;
    private int zan;
    private List<Comment> comment_list = new ArrayList<>();

    public long getRight_answer_id() {
        return right_answer_id;
    }

    public void setRight_answer_id(long right_answer_id) {
        this.right_answer_id = right_answer_id;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public List<QuestionAnswer> getAnswer() {
        answer.clear();
        answer.addAll(LitePal.where("question_id = ?", String.valueOf(id)).order("id desc").find(QuestionAnswer.class, true));
        return answer;
    }

    public List<QuestionAnswer> getLocalAnswer() {
        return answer;
    }

    public void setAnswer(List<QuestionAnswer> answer) {
        this.answer = answer;
    }

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


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", answer=" + answer +
                ", zan=" + zan +
                ", comment_list=" + comment_list +
                '}';
    }
}
