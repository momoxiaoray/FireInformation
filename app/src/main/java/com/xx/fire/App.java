package com.xx.fire;

import android.app.Application;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.hawk.Hawk;
import com.xx.fire.model.User;
import com.xx.fire.util.T;

import org.litepal.LitePal;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        LitePal.initialize(this);
        Utils.init(this);
        Hawk.init(this).build();
        T.init(this);

        //进入App就保存一个管理员
        User user = new User();
        user.setAccount("123456");
        user.setPassword("123456");
        user.setIsmanager(true);
        user.setNickname("管理员");
        user.save();
    }
}
