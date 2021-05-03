package com.xx.fire.fragment.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {
    private MutableLiveData<List<News>> newsModel;
    private List<News> newsDatas = new ArrayList<>();

    public ItemViewModel() {
        newsModel = new MutableLiveData<>();
    }

    public LiveData<List<News>> getData(int style) {
        newsDatas.clear();
        newsDatas.addAll(LitePal.where("news_type = ?", style + "").order("id desc").find(News.class, true));
        for (int i = 0; i < newsDatas.size(); i++) {
            Log.d("News", newsDatas.get(i).toString());
        }
        newsModel.postValue(newsDatas);
        return newsModel;
    }

    public void addScanCount(int position) {
        int count = newsDatas.get(position).getScan_count();
        newsDatas.get(position).setScan_count(count + 1);
        newsDatas.get(position).save();
        newsModel.postValue(newsDatas);
    }

    public void collectAction(int position) {
        User user = UserUtil.getCurrentUser();
        List<User> userList = newsDatas.get(position).getUsers();
        if (userList.contains(user)) {
            userList.remove(user);
        } else {
            userList.add(user);
        }
        newsDatas.get(position).save();
        newsModel.postValue(newsDatas);
    }
}