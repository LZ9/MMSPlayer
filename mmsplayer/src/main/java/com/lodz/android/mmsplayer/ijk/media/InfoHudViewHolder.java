package com.lodz.android.mmsplayer.ijk.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.lodz.android.mmsplayer.R;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * 播放信息打印类
 * Created by zhouL on 2016/12/1.
 */

public class InfoHudViewHolder {
    private IMediaPlayer mMediaPlayer;
    private long mLoadCost = 0;
    private long mSeekCost = 0;

    private Context mContext;

    private HudHandler mHandler;

    public InfoHudViewHolder(Context context) {
        this.mContext = context;
        mHandler = new HudHandler(mHudListener);
    }

    private void setRowValue(int id, String value) {
        Log.i(IjkVideoView.TAG_INFO_HUD, mContext.getString(id) + " : " + value);
    }

    public void setMediaPlayer(IMediaPlayer mp) {
        mMediaPlayer = mp;
        if (mMediaPlayer != null) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
        } else {
            mHandler.removeMessages(MSG_UPDATE_HUD);
        }
    }

    private static String formatedDurationMilli(long duration) {
        if (duration >= 1000) {
            return String.format(Locale.US, "%.2f sec", ((float) duration) / 1000);
        } else {
            return String.format(Locale.US, "%d msec", duration);
        }
    }

    private static String formatedSpeed(long bytes, long elapsed_milli) {
        if (elapsed_milli <= 0) {
            return "0 B/s";
        }

        if (bytes <= 0) {
            return "0 B/s";
        }

        float bytes_per_sec = ((float) bytes) * 1000.f / elapsed_milli;
        if (bytes_per_sec >= 1000 * 1000) {
            return String.format(Locale.US, "%.2f MB/s", bytes_per_sec / 1000 / 1000);
        } else if (bytes_per_sec >= 1000) {
            return String.format(Locale.US, "%.1f KB/s", bytes_per_sec / 1000);
        } else {
            return String.format(Locale.US, "%d B/s", (long) bytes_per_sec);
        }
    }

    void updateLoadCost(long time) {
        mLoadCost = time;
    }

    void updateSeekCost(long time) {
        mSeekCost = time;
    }

    private static String formatedSize(long bytes) {
        if (bytes >= 100 * 1000) {
            return String.format(Locale.US, "%.2f MB", ((float) bytes) / 1000 / 1000);
        } else if (bytes >= 100) {
            return String.format(Locale.US, "%.1f KB", ((float) bytes) / 1000);
        } else {
            return String.format(Locale.US, "%d B", bytes);
        }
    }

    private static final int MSG_UPDATE_HUD = 1;

    private static class HudHandler extends Handler {
        WeakReference<HudListener> mWeakReference;

        private HudHandler(HudListener listener) {
            mWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_HUD) {
                HudListener listener = mWeakReference.get();
                if (listener != null) {
                    listener.onUpdate();
                }
            }
        }
    }

    private HudListener mHudListener = new HudListener() {
        @Override
        public void onUpdate() {
            updateHud();
            mHandler.removeMessages(MSG_UPDATE_HUD);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
        }
    };

    private void updateHud() {
        InfoHudViewHolder holder = InfoHudViewHolder.this;
        IjkMediaPlayer mp = null;
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer instanceof IjkMediaPlayer) {
            mp = (IjkMediaPlayer) mMediaPlayer;
        } else if (mMediaPlayer instanceof MediaPlayerProxy) {
            MediaPlayerProxy proxy = (MediaPlayerProxy) mMediaPlayer;
            IMediaPlayer internal = proxy.getInternalMediaPlayer();
            if (internal instanceof IjkMediaPlayer)
                mp = (IjkMediaPlayer) internal;
        }
        if (mp == null)
            return;

        int vdec = mp.getVideoDecoder();
        switch (vdec) {
            case IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC:
                setRowValue(R.string.mmsplayer_vdec, "avcodec");
                break;
            case IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC:
                setRowValue(R.string.mmsplayer_vdec, "MediaCodec");
                break;
            default:
                setRowValue(R.string.mmsplayer_vdec, "");
                break;
        }

        float fpsOutput = mp.getVideoOutputFramesPerSecond();
        float fpsDecode = mp.getVideoDecodeFramesPerSecond();
        setRowValue(R.string.mmsplayer_fps, String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput));

        long videoCachedDuration = mp.getVideoCachedDuration();
        long audioCachedDuration = mp.getAudioCachedDuration();
        long videoCachedBytes = mp.getVideoCachedBytes();
        long audioCachedBytes = mp.getAudioCachedBytes();
        long tcpSpeed = mp.getTcpSpeed();
        long bitRate = mp.getBitRate();
        long seekLoadDuration = mp.getSeekLoadDuration();

        setRowValue(R.string.mmsplayer_v_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(videoCachedDuration), formatedSize(videoCachedBytes)));
        setRowValue(R.string.mmsplayer_a_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(audioCachedDuration), formatedSize(audioCachedBytes)));
        setRowValue(R.string.mmsplayer_load_cost, String.format(Locale.US, "%d ms", mLoadCost));
        setRowValue(R.string.mmsplayer_seek_cost, String.format(Locale.US, "%d ms", mSeekCost));
        setRowValue(R.string.mmsplayer_seek_load_cost, String.format(Locale.US, "%d ms", seekLoadDuration));
        setRowValue(R.string.mmsplayer_tcp_speed, String.format(Locale.US, "%s", formatedSpeed(tcpSpeed, 1000)));
        setRowValue(R.string.mmsplayer_bit_rate, String.format(Locale.US, "%.2f kbs", bitRate / 1000f));
        Log.i(IjkVideoView.TAG_INFO_HUD, " \n ");
    }

    private interface HudListener {
        void onUpdate();
    }
}