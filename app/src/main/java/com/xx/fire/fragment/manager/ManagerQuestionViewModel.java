package com.xx.fire.fragment.manager;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.StringUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ManagerQuestionViewModel extends ViewModel {

    private MutableLiveData<List<Question>> liveData;
    private List<Question> questions = new ArrayList<>();

    public ManagerQuestionViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void refreshData() {
        questions.clear();
        questions.addAll(LitePal.order("id desc").find(Question.class, true));
        for (int i = 0; i < questions.size(); i++) {
            Log.d("dynamics", questions.get(i).toString());
        }
        liveData.setValue(questions);
    }

    public void delete(int position) {
        questions.get(position).delete();
        questions.remove(position);
        liveData.setValue(questions);
    }

    public void answerPublish(Question question, String answer){
        question.setRight_answer_id(0);
        question.setRight_answer(answer);
        question.update(question.getId());
        refreshData();
    }

    public void answerPublish(Question question, QuestionAnswer answer){
        question.setRight_answer_id(answer.getId());
        question.setRight_answer("");
        question.update(question.getId());
        refreshData();
    }

    public LiveData<List<Question>> getData() {
        return liveData;
    }
}