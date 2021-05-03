package com.xx.fire;

import android.content.SyncAdapterType;

import com.orhanobut.hawk.Hawk;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.util.List;

public class UserUtil {
    public static final String USER_INFO = "user_info";

    public static void saveCurrentUser(String account) {
        Hawk.put(USER_INFO, getUser(account));
    }

    public static User getCurrentUser() {
        return Hawk.get(USER_INFO);
    }

    public static void exit() {
        Hawk.deleteAll();
    }

    public static boolean isManager(String account) {
        return App.getInstance().getManger().getAccount().equals(account);
    }


    public static boolean isExit(String account) {
        User user = LitePal.where("account = ?", account).findFirst(User.class);
        if (user == null) {
            return false;
        }
        return true;
    }

    public static User getUser(String account) {
        User user = LitePal.where("account = ?", account).findFirst(User.class);
        if (user == null) {
            return null;
        }
        return user;
    }

    public static boolean checkPassword(String account, String password) {
        User user = LitePal.select("account", "password").where("account = ?", account).findFirst(User.class);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }


}
