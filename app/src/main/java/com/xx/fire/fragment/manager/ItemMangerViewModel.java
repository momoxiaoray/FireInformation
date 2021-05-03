package com.xx.fire.fragment.manager;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemMangerViewModel extends ViewModel {
    private MutableLiveData<List<News>> newsModel;
    private List<News> newsDatas = new ArrayList<>();

    public ItemMangerViewModel() {
        newsModel = new MutableLiveData<>();
    }

    public LiveData<List<News>> getData(int style) {
        refreshData(style);
        return newsModel;
    }

    public void refreshData(int style) {
        newsDatas.clear();
        newsDatas.addAll(LitePal.where("news_type = ?", style + "").order("id desc").find(News.class, true));
        for (int i = 0; i < newsDatas.size(); i++) {
            Log.d("News", newsDatas.get(i).toString());
        }
        newsModel.setValue(newsDatas);
    }

    public void delete(int position) {
        newsDatas.get(position).delete();
        newsDatas.remove(position);
        newsModel.setValue(newsDatas);
    }
}