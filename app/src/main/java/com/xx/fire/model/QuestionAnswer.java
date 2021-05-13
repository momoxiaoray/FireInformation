package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionAnswer extends LitePalSupport implements Serializable {
    private long id;
    private String answer_content;// 问题答案的内容
    private List<Long> user_ids = new ArrayList<>();//问题选择的人id集合

    private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public List<Long> getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(List<Long> user_ids) {
        this.user_ids = user_ids;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                ", answer_content='" + answer_content + '\'' +
                ", user_ids=" + user_ids +
                '}';
    }
}
