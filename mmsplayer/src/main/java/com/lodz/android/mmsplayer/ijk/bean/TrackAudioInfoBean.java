package com.lodz.android.mmsplayer.ijk.bean;

/**
 * 音频轨信息实体
 * Created by zhouL on 2016/11/25.
 */
public class TrackAudioInfoBean {

    public String type = "";

    public String language = "";

    public String codec = "";

    public String profileLevel = "";

    public String sampleRate = "";

    public String channels = "";

    public String bitRate = "";

    public boolean isSelected;

    @Override
    public String toString() {
        return "TrackAudioInfoBean{" +
                " type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", codec='" + codec + '\'' +
                ", profileLevel='" + profileLevel + '\'' +
                ", sampleRate='" + sampleRate + '\'' +
                ", channels='" + channels + '\'' +
                ", bitRate='" + bitRate + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}