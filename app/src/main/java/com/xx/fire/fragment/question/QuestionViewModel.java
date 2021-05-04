package com.xx.fire.fragment.question;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.Question;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class QuestionViewModel extends ViewModel {

    private MutableLiveData<List<Question>> liveData;
    private List<Question> questions = new ArrayList<>();

    public QuestionViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void refreshData(boolean self) {
        questions.clear();
        if (self) {
            questions.addAll(LitePal.where("user_id = ?", String.valueOf(UserUtil.getCurrentUser().getId()))
                    .order("id desc").find(Question.class, true));
        } else {
            questions.addAll(LitePal.order("id desc").find(Question.class, true));
        }

        for (int i = 0; i < questions.size(); i++) {
            Log.d("dynamics", questions.get(i).toString());
        }
        liveData.setValue(questions);
    }

    public void hit(int position) {
        int zan = questions.get(position).getZan();
        questions.get(position).setZan(zan + 1);
        questions.get(position).save();
        liveData.setValue(questions);
    }

    public LiveData<List<Question>> getData() {
        return liveData;
    }
}