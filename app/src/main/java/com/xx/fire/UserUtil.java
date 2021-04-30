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

    public static void exit(){
        Hawk.deleteAll();
    }


    public static boolean isExit(String account) {
        List<User> users = LitePal.findAll(User.class);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getAccount().equals(account)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static User getUser(String account) {
        List<User> users = LitePal.findAll(User.class);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getAccount().equals(account)) {
                    return user;
                }
            }
        }
        return null;
    }

    public static boolean checkPassword(String account, String password) {
        List<User> users = LitePal.select("account", "password").where("account = ?", account).find(User.class);
        if (users != null && users.size() > 0) {
            return users.get(0).getPassWord().equals(password);
        }
        return false;
    }


}
