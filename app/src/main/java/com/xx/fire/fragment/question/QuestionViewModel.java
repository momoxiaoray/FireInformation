package com.xx.fire.fragment.question;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

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
            Log.d("questions", questions.get(i).toString());
        }
        liveData.setValue(questions);
    }

    public void hit(int position) {
        int zan = questions.get(position).getZan();
        questions.get(position).setZan(zan + 1);
        questions.get(position).save();
        liveData.setValue(questions);
    }

    public void selectAnswer(int position, int childPosition) {
        Question question = questions.get(position);
        List<QuestionAnswer> answers = question.getAnswer();
        //先移除之前的选择
        for (int i = 0; i < answers.size(); i++) {
            List<Long> userIds = answers.get(i).getUser_ids();
            if (userIds.contains(UserUtil.getCurrentUser().getId())) {
                userIds.remove(UserUtil.getCurrentUser().getId());
                answers.get(i).setUser_ids(userIds);
                answers.get(i).update(answers.get(i).getId());
            }
        }
        List<QuestionAnswer> answers2 = LitePal.order("id desc").find(QuestionAnswer.class, true);
        for (int i = 0; i < answers2.size(); i++) {
            Log.d("answers2", answers2.get(i).toString());
        }
        List<Long> userIds = answers.get(childPosition).getUser_ids();
        userIds.add(UserUtil.getCurrentUser().getId());
        answers.get(childPosition).setUser_ids(userIds);
        answers.get(childPosition).update(answers.get(childPosition).getId());
        question.setAnswer(answers);
        question.update(question.getId());
        List<Question> questionList = LitePal.order("id desc").find(Question.class, true);
        for (int i = 0; i < questionList.size(); i++) {
            Log.d("questions", questionList.get(i).toString());
        }
        List<QuestionAnswer> answers1 = LitePal.order("id desc").find(QuestionAnswer.class, true);
        for (int i = 0; i < answers1.size(); i++) {
            Log.d("answers", answers1.get(i).toString());
        }
        liveData.setValue(questions);
    }

    public LiveData<List<Question>> getData() {
        return liveData;
    }
}