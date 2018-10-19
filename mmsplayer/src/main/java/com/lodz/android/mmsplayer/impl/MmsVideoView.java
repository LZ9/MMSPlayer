package com.lodz.android.mmsplayer.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import androidx.annotation.IntDef;
import com.lodz.android.mmsplayer.R;
import com.lodz.android.mmsplayer.contract.IVideoPlayer;
import com.lodz.android.mmsplayer.ijk.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 播放器
 * Created by zhouL on 2017/2/15.
 */
public class MmsVideoView extends IjkVideoView implements IVideoPlayer {

    public MmsVideoView(Context context) {
        super(context);
    }

    public MmsVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MmsVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MmsVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        stopPlayback();
        release(true);
        stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }


    @Override
    public void setListener(final Listener listener) {
        if (listener == null) {
            return;
        }

        setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                listener.onPrepared();
            }
        });

        setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                listener.onCompletion();
            }
        });

        setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int arg1, int arg2) {
                switch (arg1) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        listener.onBufferingStart();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        listener.onBufferingEnd();
                        break;
                }
                return false;
            }
        });

        setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
                int errorType = Listener.ErrorType.UNKNOWN;
                String msg = getContext().getString(R.string.mmsplayer_videoview_error_text_unknown);
                switch (framework_err) {
                    case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                        errorType = Listener.ErrorType.INVALID_PROGRESSIVE_PLAYBACK;
                        msg = getContext().getString(R.string.mmsplayer_videoview_error_text_invalid_progressive_playback);
                        break;
                    case -10000:
                        errorType = Listener.ErrorType.NETWORK_ERROR;
                        msg = getContext().getString(R.string.mmsplayer_videoview_error_text_io);
                        break;
                }
                listener.onError(errorType, msg);
                return true;
            }
        });
    }

    @Override
    public void seekAndStart(long position) {
        seekTo(position);
        if (!isPlaying()) {
            start();
        }
    }

    public interface Listener {
        @Retention(RetentionPolicy.SOURCE)
        @IntDef({ErrorType.UNKNOWN, ErrorType.INVALID_PROGRESSIVE_PLAYBACK, ErrorType.NETWORK_ERROR})
        @interface ErrorType {
            int UNKNOWN = 0;

            int INVALID_PROGRESSIVE_PLAYBACK = 1;

            int NETWORK_ERROR = 2;
        }


        /**
         * 播放器就绪
         */
        void onPrepared();

        /**
         * 缓冲开始
         */
        void onBufferingStart();

        /**
         * 缓冲结束
         */
        void onBufferingEnd();

        /**
         * 播放完成
         */
        void onCompletion();

        /**
         * 错误回调
         *
         * @param errorType 错误类型
         * @param msg       错误信息
         */
        void onError(@ErrorType int errorType, String msg);

    }

}
