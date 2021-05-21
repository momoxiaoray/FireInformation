package com.xx.fire.activity.manager.quesition;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class QuestionAddViewModel extends ViewModel {

    private MutableLiveData<Question> liveData;
    private List<QuestionAnswer> answerList = new ArrayList<>();
    private  Question question ;
    public QuestionAddViewModel() {
        liveData = new MutableLiveData<>();
        question = new Question();
    }

    public void save(String content) {
        question.setContent(content);
        question.setDate(TimeUtils.getNowString());
        question.save();
    }

    public boolean checkSave() {
        if (answerList.size() < 2) {
            return false;
        }
        return true;
    }

    public void addAnswer(String answerContent) {
        QuestionAnswer answer = new QuestionAnswer();
        answer.setAnswer_content(answerContent);
        answer.save();
        answerList.add(answer);
        question.setAnswer(answerList);
        liveData.setValue(question);
    }

    public void deleteAnswer(QuestionAnswer answer) {
        answer.delete();
        answerList.remove(answer);
        question.setAnswer(answerList);
        liveData.setValue(question);
    }

    public List<QuestionAnswer> getAnswers() {
        return answerList;
    }

    public void saveRightAnswerId(QuestionAnswer answer) {
        question.setRight_answer_id(answer.getId());
    }


    public LiveData<Question> getData() {
        return liveData;
    }
}