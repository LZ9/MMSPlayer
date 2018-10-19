package com.lodz.android.mmsplayer.ijk.bean;

/**
 * 视频轨信息实体
 * Created by zhouL on 2016/11/25.
 */
public class TrackVideoInfoBean {

    public String type = "";

    public String language = "";

    public String codec = "";

    public String profileLevel = "";

    public String pixelFormat = "";

    public String resolution = "";

    public String frameRate = "";

    public String bitRate = "";

    public boolean isSelected;

    @Override
    public String toString() {
        return "TrackVideoInfoBean{" +
                " type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", codec='" + codec + '\'' +
                ", profileLevel='" + profileLevel + '\'' +
                ", pixelFormat='" + pixelFormat + '\'' +
                ", resolution='" + resolution + '\'' +
                ", frameRate='" + frameRate + '\'' +
                ", bitRate='" + bitRate + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
