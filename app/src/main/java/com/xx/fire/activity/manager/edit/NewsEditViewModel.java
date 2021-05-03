package com.xx.fire.activity.manager.edit;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.News;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class NewsEditViewModel extends ViewModel {

    private MutableLiveData<News> liveData;
    private News data;

    public NewsEditViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void initData(News data) {
        if (data == null) {
            this.data = new News();
        } else {
            this.data = data;
            liveData.setValue(data);
        }
    }

    public void save(String title, String content, int type) {
        //大于0表示已经在数据库，进行更新操作
        if (data.getId() > 0) {
            data.setTitle(title);
            data.setContent(content);
            data.update(data.getId());
        } else {
            data.setTitle(title);
            data.setContent(content);
            data.setNews_type(type);
            data.setDate(TimeUtils.getNowString());
            data.save();
        }
        liveData.setValue(data);
    }

    public void removePic() {
        data.setCoverPath(null);
        liveData.setValue(data);
    }

    public void addPic(String path) {
        data.setCoverPath(path);
        liveData.setValue(data);
    }

    public News getData(){
        return data;
    }


    public LiveData<News> getLiveData() {
        return liveData;
    }
}