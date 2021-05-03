package com.xx.fire;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.hawk.Hawk;
import com.xx.fire.model.User;
import com.xx.fire.util.T;

import org.litepal.LitePal;

public class App extends Application {

    private User manger;
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MultiDex.install(this);
        LitePal.initialize(this);
        Utils.init(this);
        Hawk.init(this).build();
        T.init(this);

        //进入App就保存一个管理员
        manger = new User();
        manger.setAccount("123456");
        manger.setPassword("123456");
        manger.setIsmanager(true);
        manger.setNickname("管理员");
        manger.save();

        User user = new User();
        user.setAccount("12345");
        user.setPassword("12345");
        user.setIsmanager(false);
        user.setNickname("测试账号1");
        user.save();
    }

    public static synchronized App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }


    public User getManger() {
        return manger;
    }
}
