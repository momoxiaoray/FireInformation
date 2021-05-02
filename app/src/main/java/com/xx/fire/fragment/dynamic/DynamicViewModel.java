package com.xx.fire.fragment.dynamic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.ArrayUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.UserUtil;
import com.xx.fire.model.Dynamic;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicViewModel extends ViewModel {

    private MutableLiveData<List<Dynamic>> liveData;
    private List<Dynamic> dynamics;

    public DynamicViewModel() {
        liveData = new MutableLiveData<>();
        dynamics = LitePal.findAll(Dynamic.class, true);
        liveData.postValue(dynamics);
    }

    public void refreshData() {
        dynamics.clear();
        dynamics.addAll(LitePal.findAll(Dynamic.class, true));
        Collections.reverse(dynamics);

        for (int i = 0; i < dynamics.size(); i++) {
            Log.d("dynamics", dynamics.get(i).toString());
        }
        liveData.setValue(dynamics);
    }

    public void hit(int position) {
        int zan = dynamics.get(position).getZan();
        dynamics.get(position).setZan(zan + 1);
        dynamics.get(position).save();
        liveData.setValue(dynamics);
    }

    public LiveData<List<Dynamic>> getData() {
        return liveData;
    }
}