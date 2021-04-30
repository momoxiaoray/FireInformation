package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {

    private String account;
    private String nickname;
    private String password;
    private boolean ismanager = false;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassWord(String passWord) {
        this.password = passWord;
    }

    public boolean isManager() {
        return ismanager;
    }

    public void setManager(boolean manager) {
        ismanager = manager;
    }
}
