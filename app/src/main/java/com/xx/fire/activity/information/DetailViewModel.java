package com.xx.fire.activity.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.model.News;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<News> newsModel;

    public DetailViewModel() {
        newsModel = new MutableLiveData<>();
    }

    public void setData(News news) {
        newsModel.postValue(news);
    }

    public LiveData<News> getData() {
        return newsModel;
    }
}