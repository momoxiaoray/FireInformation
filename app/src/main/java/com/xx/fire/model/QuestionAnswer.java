package com.xx.fire.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionAnswer extends LitePalSupport implements Serializable {
    private long id;
    private String answer_content;// 问题答案的内容
    //问题选择的人id集合
    private List<QuestionAnswerUser> user_ids = new ArrayList<>();

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

    public List<QuestionAnswerUser> getUser_ids() {
        user_ids.clear();
        user_ids.addAll(LitePal.where("answer_id = ?", String.valueOf(id)).order("id desc").find(QuestionAnswerUser.class, true));
        return user_ids;
    }

    public void setUser_ids(List<QuestionAnswerUser> user_ids) {
        this.user_ids = user_ids;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "id=" + id +
                ", answer_content='" + answer_content + '\'' +
                ", user_ids=" + user_ids +
                ", question=" + question +
                '}';
    }
}
