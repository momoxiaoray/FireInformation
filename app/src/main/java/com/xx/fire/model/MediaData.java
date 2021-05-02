package com.xx.fire.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class MediaData extends LitePalSupport implements Serializable {

    private int type;//0图片、1视频、-1站位
    private String path;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return "MediaData{" +
                "type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
