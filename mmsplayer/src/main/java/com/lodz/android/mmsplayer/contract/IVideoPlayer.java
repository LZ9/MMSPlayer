package com.lodz.android.mmsplayer.contract;

import android.content.Context;
import com.lodz.android.mmsplayer.ijk.bean.MediaInfoBean;
import com.lodz.android.mmsplayer.ijk.media.IMediaController;
import com.lodz.android.mmsplayer.ijk.setting.IjkPlayerSetting;
import com.lodz.android.mmsplayer.impl.MmsVideoView;

/**
 * 播放器接口
 * Created by zhouL on 2016/11/30.
 */
public interface IVideoPlayer {

    /** 初始化 */
    void init();

    /** 初始化 */
    void init(IjkPlayerSetting setting);

    /** 设置原生控制器 */
    void setMediaController(IMediaController controller);

    /** 设置播放路径 */
    void setVideoPath(String path);

    /** 开始播放 */
    void start();

    /** 是否正在播放 */
    boolean isPlaying();

    /** 是否暂停 */
    boolean isPause();

    /** 是否播放结束 */
    boolean isCompleted();

    /** 是否已经设置播放地址 */
    boolean isAlreadySetPath();

    /** 暂停 */
    void pause();

    /** 重新播放 */
    void resume();

    /** 停止播放释放资源 */
    void release();

    /** 获取视频信息 */
    MediaInfoBean getMediaInfo();

    /** 切换宽高比 */
    int toggleAspectRatio();

    /**
     * 设置宽高比
     * @param aspectRatioType 宽高比类型
     */
    void setAspectRatio(int aspectRatioType);

    /** 获取当前渲染view名称 */
    String getRenderText(Context context);

    /** 获取当前播放器名称 */
    String getPlayerText(Context context);

    /** 获取缓存进度百分比 */
    int getBufferPercentage();

    /** 获取当前播放进度 */
    long getCurrentPlayPosition();

    /** 获取播放失败时的进度 */
    long getBreakPosition();

    /** 获取视频总时长 */
    long getVideoDuration();

    /** 设置监听器回调 */
    void setListener(MmsVideoView.Listener listener);

    /**
     * 设置播放位置
     * @param position 进度
     */
    void seekTo(long position);

    /**
     * 设置播放位置并开始播放
     * @param position 进度
     */
    void seekAndStart(long position);

    /**
     * 设置倍数（未生效，请勿调用）
     * @param speed 倍数
     */
    void setSpeed(float speed);
}