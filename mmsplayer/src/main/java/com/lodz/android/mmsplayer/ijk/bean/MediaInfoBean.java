package com.lodz.android.mmsplayer.ijk.bean;

/**
 * 视频信息实体
 * Created by zhouL on 2016/11/25.
 */

public class MediaInfoBean {

// ------------------- 播放器信息 -----------------
    /** 播放器名称 */
    public String playerName = "";

// ------------------- 视频信息 -----------------
    /** 分辨率 */
    public String resolution = "";
    /** 视频长度 */
    public String length = "";

// ------------------- 视频轨 -----------------
    /** 视频轨信息实体 */
    public TrackVideoInfoBean trackVideoInfoBean;

// ------------------- 音频轨 -----------------
    /** 音频轨信息实体 */
    public TrackAudioInfoBean trackAudioInfoBean;


    @Override
    public String toString() {
        return "MediaInfoBean{" +
                "\n playerName='" + playerName + '\'' +
                ",\n resolution='" + resolution + '\'' +
                ",\n length='" + length + '\'' +
                ",\n trackVideoInfoBean=" + trackVideoInfoBean +
                ",\n trackAudioInfoBean=" + trackAudioInfoBean +
                '}';
    }
}
