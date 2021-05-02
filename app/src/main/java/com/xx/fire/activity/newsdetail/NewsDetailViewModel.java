package com.xx.fire.activity.newsdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.model.News;

public class NewsDetailViewModel extends ViewModel {

    private MutableLiveData<News> newsModel;

    public NewsDetailViewModel() {
        newsModel = new MutableLiveData<>();
    }

    public void setData(News news) {
        newsModel.postValue(news);
    }

    public LiveData<News> getData() {
        return newsModel;
    }
}