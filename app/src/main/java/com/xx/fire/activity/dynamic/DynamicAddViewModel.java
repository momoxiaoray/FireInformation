package com.xx.fire.activity.dynamic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import java.util.ArrayList;
import java.util.List;

public class DynamicAddViewModel extends ViewModel {

    private MutableLiveData<List<MediaData>> liveData;
    private List<MediaData> mediaDataList = new ArrayList<>();

    public DynamicAddViewModel() {
        liveData = new MutableLiveData<>();
        MediaData data = new MediaData();
        data.setType(-1);
        mediaDataList.add(data);
        liveData.setValue(mediaDataList);
    }

    public void saveDynamic(String content) {
        Dynamic dynamic = new Dynamic();
        dynamic.setDate(TimeUtils.getNowString());
        dynamic.setUser(UserUtil.getCurrentUser());
        dynamic.setDynamic_content(content);
        dynamic.setZan(0);
        List<MediaData> list = new ArrayList<>();
        for (int i = 0; i < mediaDataList.size(); i++) {
            if (mediaDataList.get(i).getType() != -1) {
                mediaDataList.get(i).save();
                list.add(mediaDataList.get(i));
            }
        }
        dynamic.setMedia_list(list);
        dynamic.save();
    }

    public void add(String path, boolean isPic) {
        MediaData mediaData = new MediaData();
        mediaData.setType(isPic ? 0 : 1);
        mediaData.setPath(path);
        mediaDataList.add(0, mediaData);
        liveData.setValue(mediaDataList);
    }

    public void remove(int position) {
        mediaDataList.remove(position);
        liveData.setValue(mediaDataList);
    }

    public LiveData<List<MediaData>> getData() {
        return liveData;
    }
}