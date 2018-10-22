package com.lodz.android.mmsplayerdemo.video.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import com.lodz.android.mmsplayer.contract.IVideoPlayer
import com.lodz.android.mmsplayer.impl.MmsVideoView
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.utils.sp.SpManager
import com.lodz.android.mmsplayerdemo.video.menu.SlideControlLayout
import com.lodz.android.mmsplayerdemo.video.menu.VideoBottomMenuLayout

/**
 * 带控制器的播放控件
 * Created by zhouL on 2018/10/22.
 */
class MediaView : FrameLayout {

    companion object {
        const val TAG = "MediaView"
    }

    /** 手势划动回调控件 */
    private val mSlideControlLayout by lazy {
        findViewById<SlideControlLayout>(R.id.slide_control_layout)
    }
    /** 视频播放器 */
    private val mVideoPlayer: IVideoPlayer by lazy {
        findViewById<MmsVideoView>(R.id.video_view)
    }
    /** 底部菜单 */
    private val mBottomMenuLayout by lazy {
        findViewById<VideoBottomMenuLayout>(R.id.bottom_menu_layout)
    }

    private var mActivity: Activity? = null
    /** 是否能播放 */
    private var isPlay = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
            context, attrs, defStyleAttr, defStyleRes
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_media, this)
        setListeners()
        initData()
    }

    private fun setListeners() {

        mVideoPlayer.setListener(object : MmsVideoView.Listener {
            override fun onPrepared() {
                mBottomMenuLayout.initConfig(mVideoPlayer.currentPlayPosition, mVideoPlayer.videoDuration)
            }

            override fun onBufferingStart() {
            }

            override fun onBufferingEnd() {
            }

            override fun onCompletion() {
                mBottomMenuLayout.showPauseStatus()
                mBottomMenuLayout.stopUpdateProgress()
                mBottomMenuLayout.setPlayCompletion()
            }

            override fun onError(errorType: Int, msg: String?) {
            }

        })

        mBottomMenuLayout.setListener(object : VideoBottomMenuLayout.Listener {
            override fun onClickPlay() {
                start()
            }

            override fun onClickPause() {
                pause()
            }

            override fun onClickSetting() {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onSeekChangedFromUser(position: Long, duration: Long) {
                mVideoPlayer.seekTo(position)
                if (mVideoPlayer.isPause || mVideoPlayer.isCompleted){
                    start()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

            override fun getBufferPercentage(): Long = mVideoPlayer.bufferPercentage.toLong()

            override fun getCurrentPlayPosition(): Long = mVideoPlayer.currentPlayPosition

        })

        mSlideControlLayout.setListener(object : SlideControlLayout.Listener {
            override fun onClick(view: View) {
            }

            override fun onStartSlideLeftZone() {
            }

            override fun onSlidingLeftZone(delta: Float) {
            }

            override fun onEndSlideLeftZone() {
            }

            override fun onStartSlideRightZone() {
            }

            override fun onSlidingRightZone(delta: Float) {
            }

            override fun onEndSlideRightZone() {
            }

            override fun onStartSlideHorizontal() {
            }

            override fun onSlidingHorizontal(delta: Float) {
            }

            override fun onEndSlideHorizontal() {
                if (mVideoPlayer.isPause || mVideoPlayer.isCompleted) {
                    start()
                }
            }
        })
    }

    private fun initData() {
        mVideoPlayer.init()
        mVideoPlayer.setAspectRatio(SpManager.get().getAspectRatioType()) // 设置宽高比
    }

    /** 初始化MediaView */
    fun initMediaView(activity: Activity) {
        mActivity = activity
    }

    /** 设置播放路径[path] */
    fun setVideoPath(path: String) {
        mVideoPlayer.setVideoPath(path)
    }

    /** 是否暂停 */
    fun isPause() = mVideoPlayer.isPause

    /** 是否正在播放 */
    fun isPlaying() = mVideoPlayer.isPlaying

    /** 开始播放 */
    fun start(){
        mVideoPlayer.start()
    }

    /** 暂停 */
    fun pause() {
        mVideoPlayer.pause()
    }

    /** 重头播放 */
    fun resume() {
        mVideoPlayer.resume()
    }

    /** 释放资源 */
    fun release() {
        mVideoPlayer.release()
    }

    /** 菜单是否显示 */
    private fun isMenuShow() = mBottomMenuLayout.isMenuShow()

    /** 显示菜单 */
    private fun showMenu(){
        mBottomMenuLayout.showMenu()
    }

    /** 隐藏菜单 */
    private fun hideMenu(){
        mBottomMenuLayout.hideMenu()
    }
}