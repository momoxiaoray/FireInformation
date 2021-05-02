package com.xx.fire.fragment.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xx.fire.UserUtil;
import com.xx.fire.model.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user;

    public UserViewModel() {
        user = new MutableLiveData<>();
        user.postValue(UserUtil.getCurrentUser());
    }

    public LiveData<User> getUser() {
        return user;
    }
}