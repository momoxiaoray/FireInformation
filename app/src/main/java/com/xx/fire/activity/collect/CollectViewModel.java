package com.xx.fire.activity.collect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CollectViewModel extends ViewModel {

    private MutableLiveData<List<News>> newsModel;
    private List<News> newsList = new ArrayList<>();

    public CollectViewModel() {
        newsModel = new MutableLiveData<>();
        List<News> newsListData = LitePal.findAll(News.class, true);
        for (int i = 0; i < newsListData.size(); i++) {
            if (newsListData.get(i).getUsers().contains(UserUtil.getCurrentUser())) {
                newsList.add(newsListData.get(i));
            }
        }
        newsModel.setValue(newsList);
    }

    public LiveData<List<News>> getData() {
        return newsModel;
    }

    public void remove(int position) {
        User user = UserUtil.getCurrentUser();
        List<User> userList = newsList.get(position).getUsers();
        if (userList.contains(user)) {
            userList.remove(user);
        } else {
            userList.add(user);
        }
        newsList.get(position).save();
        newsList.remove(position);
        newsModel.postValue(newsList);
    }
}