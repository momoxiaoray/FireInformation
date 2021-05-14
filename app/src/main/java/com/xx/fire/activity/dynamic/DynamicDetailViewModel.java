package com.xx.fire.activity.dynamic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailViewModel extends ViewModel {

    private MutableLiveData<Dynamic> liveData;
    private Dynamic dynamic;

    public DynamicDetailViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void initData(Dynamic dynamic) {
        this.dynamic = dynamic;
        liveData.setValue(dynamic);
    }

    public void hit() {
        int zan = dynamic.getZan();
        dynamic.setZan(zan + 1);
        dynamic.update(dynamic.getId());
        liveData.setValue(dynamic);
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
        comment.setDynamic_id(dynamic.getId());
        comment.save();
        dynamic.getComment_list().add(comment);
        dynamic.update(dynamic.getId());
        liveData.setValue(dynamic);
        List<Comment> comments = LitePal.findAll(Comment.class, true);
        for (int i = 0; i < comments.size(); i++) {
            Log.d("comment", comments.get(i).toString());
        }
    }


    public LiveData<Dynamic> getData() {
        return liveData;
    }
}