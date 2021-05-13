package com.xx.fire.activity.question;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

import org.litepal.LitePal;

import java.util.List;

public class QuestionDetailViewModel extends ViewModel {

    private MutableLiveData<Question> liveData;
    private Question question;

    public QuestionDetailViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void initData(Question question) {
        this.question = question;
        liveData.setValue(question);
    }

    public void hit() {
        int zan = question.getZan();
        question.setZan(zan + 1);
        question.saveOrUpdate();
        liveData.setValue(question);
    }

    public void selectAnswer(int childPosition) {
        List<QuestionAnswer> answers = question.getAnswer();
        for (int i = 0; i < answers.size(); i++) {
            List<Long> userIds = answers.get(i).getUser_ids();
            if (userIds.contains(UserUtil.getCurrentUser().getId())) {
                userIds.remove(UserUtil.getCurrentUser().getId());
                answers.get(i).setUser_ids(userIds);
            }
        }
        List<Long> userIds = answers.get(childPosition).getUser_ids();
        userIds.add(UserUtil.getCurrentUser().getId());
        answers.get(childPosition).setUser_ids(userIds);
//        answers.get(childPosition).update(answers.get(childPosition).getId());
        question.setAnswer(answers);
        question.update(question.getId());
    }

    public void addComment(Comment commentCome, String content) {
        //如果评论人为空，怎表示刚刚new的新评论
        Comment comment = new Comment();
        if (commentCome == null) {
            comment.setStyle(0);
        } else {
            comment.setStyle(1);
            comment.setRecover_user(commentCome.getUser().getNickname());
            comment.setRecover_content(commentCome.getComment_content());
        }
        comment.setUser_id(UserUtil.getCurrentUser().getId());
        comment.setUser(UserUtil.getCurrentUser());
        comment.setDate(TimeUtils.getNowString());
        comment.setComment_content(content);
        comment.setQuestion_id(question.getId());
        comment.save();
        question.getComment_list().add(comment);
        question.update(question.getId());
        liveData.setValue(question);
        List<Comment> comments = LitePal.findAll(Comment.class, true);
        for (int i = 0; i < comments.size(); i++) {
            Log.d("comment", comments.get(i).toString());
        }
    }


    public LiveData<Question> getData() {
        return liveData;
    }
}