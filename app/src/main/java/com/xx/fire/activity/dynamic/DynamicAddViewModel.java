package com.xx.fire.activity.dynamic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.TimeUtils;
import com.huantansheng.easyphotos.models.album.entity.Photo;
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
    private ArrayList<Photo> imageItems = new ArrayList<>();

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
        dynamic.setUser_id(UserUtil.getCurrentUser().getId());
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

    public void remove(int position) {
        mediaDataList.remove(position);
        liveData.setValue(mediaDataList);
    }

    public ArrayList<Photo> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<Photo> imageItems) {
        if (imageItems == null)
            return;
        this.imageItems.clear();
        this.imageItems.addAll(imageItems);
        for (int i = 0; i < imageItems.size(); i++) {
            MediaData mediaData = new MediaData();
            mediaData.setType(imageItems.get(i).type.startsWith("video") ? 1 : 0);
            mediaData.setPath(imageItems.get(i).path);
            mediaDataList.add(0, mediaData);
        }
        liveData.setValue(mediaDataList);
    }

    public List<MediaData> getMediaData() {
        List<MediaData> medias = new ArrayList<>();
        for (int i = 0; i < mediaDataList.size(); i++) {
            if (mediaDataList.get(i).getType() != -1) {
                medias.add(mediaDataList.get(i));
            }
        }
        return medias;
    }

    public LiveData<List<MediaData>> getData() {
        return liveData;
    }
}