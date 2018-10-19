package com.lodz.android.mmsplayer.ijk.utils;

import android.content.Context;
import android.text.TextUtils;
import com.lodz.android.mmsplayer.R;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

import java.util.Locale;

/**
 * 视频信息帮助类
 * Created by zhouL on 2016/12/1.
 */
public class MediaInfoUtils {

    /**
     * 格式化分辨率
     * @param width 视频宽度
     * @param height 视频高度
     */
    public static String buildResolution(int width, int height, int sarNum, int sarDen) {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(" x ");
        sb.append(height);

        if (sarNum > 1 || sarDen > 1) {
            sb.append("[");
            sb.append(sarNum);
            sb.append(":");
            sb.append(sarDen);
            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * 格式化时间
     * @param duration 时长
     */
    public static String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (duration <= 0) {
            return "00:00";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    /**
     * 获取轨道名称
     * @param context 上下文
     * @param type 轨道类型
     */
    public static String buildTrackType(Context context, int type) {
        switch (type) {
            case ITrackInfo.MEDIA_TRACK_TYPE_VIDEO:
                return context.getString(R.string.mmsplayer_tracktype_video);
            case ITrackInfo.MEDIA_TRACK_TYPE_AUDIO:
                return context.getString(R.string.mmsplayer_tracktype_audio);
            case ITrackInfo.MEDIA_TRACK_TYPE_SUBTITLE:
                return context.getString(R.string.mmsplayer_tracktype_subtitle);
            case ITrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT:
                return context.getString(R.string.mmsplayer_tracktype_timedtext);
            case ITrackInfo.MEDIA_TRACK_TYPE_METADATA:
                return context.getString(R.string.mmsplayer_tracktype_metadata);
            case ITrackInfo.MEDIA_TRACK_TYPE_UNKNOWN:
            default:
                return context.getString(R.string.mmsplayer_tracktype_unknown);
        }
    }

    public static String buildLanguage(String language) {
        return TextUtils.isEmpty(language)? "N/A" : language;
    }
}