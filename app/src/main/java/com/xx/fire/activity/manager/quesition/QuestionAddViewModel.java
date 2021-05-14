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

    private MutableLiveData<List<QuestionAnswer>> liveData;
    private List<QuestionAnswer> answerList = new ArrayList<>();

    public QuestionAddViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void save(String content) {
        Question question = new Question();
        question.setContent(content);
        if (answerList.size() > 0) {
            for (int i = 0; i < answerList.size(); i++) {
                answerList.get(i).save();
            }
        }
        question.setAnswer(answerList);
        question.setDate(TimeUtils.getNowString());
        question.save();
    }

    public boolean checkSave() {
        if (answerList.size() == 1) {
            return false;
        }
        return true;
    }

    public void addAnswer(String answerContent) {
        QuestionAnswer answer = new QuestionAnswer();
        answer.setAnswer_content(answerContent);
        answerList.add(answer);
        liveData.setValue(answerList);
    }

    public void deleteAnswer(QuestionAnswer answer) {
        answerList.remove(answer);
        liveData.setValue(answerList);
    }


    public LiveData<List<QuestionAnswer>> getData() {
        return liveData;
    }
}