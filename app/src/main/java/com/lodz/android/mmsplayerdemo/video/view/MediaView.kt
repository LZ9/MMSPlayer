package com.lodz.android.mmsplayerdemo.video.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import com.lodz.android.mmsplayer.contract.IVideoPlayer
import com.lodz.android.mmsplayer.ijk.media.IRenderView
import com.lodz.android.mmsplayer.ijk.setting.IjkPlayerSetting
import com.lodz.android.mmsplayer.impl.MmsVideoView
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.config.Constant
import com.lodz.android.mmsplayerdemo.video.assist.VideoAdjustProgressLayout
import com.lodz.android.mmsplayerdemo.video.assist.VideoBrightnessLayout
import com.lodz.android.mmsplayerdemo.video.assist.VideoVolumeLayout
import com.lodz.android.mmsplayerdemo.video.dialog.VideoSettingDialog
import com.lodz.android.mmsplayerdemo.video.menu.SlideControlLayout
import com.lodz.android.mmsplayerdemo.video.menu.VideoBottomMenuLayout
import com.lodz.android.mmsplayerdemo.video.menu.VideoTopMenuLayout
import com.lodz.android.mmsplayerdemo.video.status.VideoErrorLayout
import com.lodz.android.mmsplayerdemo.video.status.VideoLoadingLayout
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 带控制器的播放控件
 * Created by zhouL on 2018/10/22.
 */
class MediaView : FrameLayout {

    companion object {
        const val TAG = "MediaView"
    }

    /** 加载页面 */
    private val mVideoLoadingLayout by lazy {
        findViewById<VideoLoadingLayout>(R.id.video_loading_layout)
    }
    /** 加载失败页 */
    private val mVideoErrorLayout by lazy {
        findViewById<VideoErrorLayout>(R.id.video_error_layout)
    }
    /** 手势划动回调控件 */
    private val mSlideControlLayout by lazy {
        findViewById<SlideControlLayout>(R.id.slide_control_layout)
    }
    /** 视频播放器 */
    private val mVideoPlayer: IVideoPlayer by lazy {
        findViewById<MmsVideoView>(R.id.video_view)
    }
    /** 顶部菜单 */
    private val mVideoTopMenuLayout by lazy {
        findViewById<VideoTopMenuLayout>(R.id.top_menu_layout)
    }
    /** 底部菜单 */
    private val mBottomMenuLayout by lazy {
        findViewById<VideoBottomMenuLayout>(R.id.bottom_menu_layout)
    }
    /** 进度调整页面 */
    private val mAdjustProgressLayout by lazy {
        findViewById<VideoAdjustProgressLayout>(R.id.adjust_progress_lyout)
    }
    /** 加载框 */
    private val mmBufferProgressBar by lazy {
        findViewById<ProgressBar>(R.id.buffer_progress_bar)
    }
    /** 亮度页面 */
    private val mVideoBrightnessLayout by lazy {
        findViewById<VideoBrightnessLayout>(R.id.video_brightness_layout)
    }
    /** 音量页面 */
    private val mVideoVolumeLayout by lazy {
        findViewById<VideoVolumeLayout>(R.id.video_volume_layout)
    }

    /** Activity */
    private var mActivity: Activity? = null
    /** 监听器 */
    private var mListener: Listener? = null
    /** 是否播放 */
    private var isPlay = false
    /** 自动隐藏菜单观察者 */
    private var mAutoHideMenuObserver: Observer<Long>? = null
    /** 订阅者 */
    private var mDisposable: Disposable? = null
    /** 是否全屏 */
    private var isFullScreen = false
    /** 播放中断时的播放进度 */
    private var mBreakPosition = 0L

    /** 播放类型 */
    private var mPlayType = Constant.UN_NEXT
    /** 宽高比 */
    private var mAspectRatioType = IRenderView.AR_ASPECT_FIT_PARENT

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_media, this)
        setListeners()
        initData()
    }

    private fun setListeners() {

        // 视频播放器
        mVideoPlayer.setListener(object : MmsVideoView.Listener {
            override fun onPrepared() {
                isPlay = true
                showMenu()
                mVideoLoadingLayout.showAnalysisUrlComplete()
                mBottomMenuLayout.init(mVideoPlayer.currentPlayPosition, mVideoPlayer.videoDuration)
                mSlideControlLayout.isCanUse = true
                mVideoLoadingLayout.hide()
                Log.v(MediaView.TAG, "播放器 ---> onPrepared")
            }

            override fun onBufferingStart() {
                mmBufferProgressBar.visibility = View.VISIBLE
                Log.v(MediaView.TAG, "播放器 ---> 开始缓冲")
            }

            override fun onBufferingEnd() {
                mmBufferProgressBar.visibility = View.GONE
                Log.v(MediaView.TAG, "播放器 ---> 结束缓冲")
            }

            override fun onCompletion() {
                mBottomMenuLayout.showPauseStatus()
                mBottomMenuLayout.stopUpdateProgress()
                mBottomMenuLayout.setPlayCompletion()
                handleCompletion(mPlayType)
                showMenu()// 播放结束时显示菜单
                stopAutoHideMenu()
                Log.v(MediaView.TAG, "播放器 ---> 播放结束")
            }

            override fun onError(errorType: Int, msg: String?) {
                if (mVideoErrorLayout.isShow()) {// 如果播放错误页已经显示则不再重复处理
                    return
                }
                mSlideControlLayout.isCanUse = false
                mBottomMenuLayout.stopUpdateProgress()
                mVideoErrorLayout.show()
                mVideoLoadingLayout.showAnalysisError()
                mVideoLoadingLayout.hide()
                mBreakPosition = mVideoPlayer.breakPosition
                Log.v(MediaView.TAG, "播放器 ---> 中断进度：$mBreakPosition   播放异常：$msg")
            }

        })

        // 顶部菜单
        mVideoTopMenuLayout.setBackListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onClickBack()
            }
        })

        // 顶部菜单 设置按钮
        mVideoTopMenuLayout.setSettingListener(OnClickListener {
            showSettingDialog()
        })

        // 底部菜单
        mBottomMenuLayout.setListener(object : VideoBottomMenuLayout.Listener {
            override fun onClickPlay() {
                start()
                updateAutoHideMenu()
            }

            override fun onClickPause() {
                pause()
                stopAutoHideMenu()
            }

            override fun onClickScreen() {
                if (mListener != null) {
                    mListener!!.onScreenChange(!isFullScreen)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                stopAutoHideMenu()
            }

            override fun onSeekChangedFromUser(position: Long, duration: Long) {
                mVideoPlayer.seekTo(position)
                if (mVideoPlayer.isPause || mVideoPlayer.isCompleted) {
                    start()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                updateAutoHideMenu()
            }

            override fun getBufferPercentage(): Long = mVideoPlayer.bufferPercentage.toLong()

            override fun getCurrentPlayPosition(): Long = mVideoPlayer.currentPlayPosition

        })

        // 手势划动回调控件
        mSlideControlLayout.setListener(object : SlideControlLayout.Listener {
            override fun onClick(view: View) {
                if (isMenuShow()) {
                    hideMenu()
                } else {
                    showMenu()
                }
            }

            override fun onStartSlideLeftZone() {
                mVideoBrightnessLayout.show()
            }

            override fun onSlidingLeftZone(delta: Float) {
                if (mActivity != null) {
                    mVideoBrightnessLayout.updateBrightness(mActivity!!, delta)
                }
            }

            override fun onEndSlideLeftZone() {
                mVideoBrightnessLayout.hide()
            }

            override fun onStartSlideRightZone() {
                mVideoVolumeLayout.show()
            }

            override fun onSlidingRightZone(delta: Float) {
                if (mActivity != null) {
                    mVideoVolumeLayout.updateVolume(mActivity!!, delta)
                }
            }

            override fun onEndSlideRightZone() {
                mVideoVolumeLayout.hide()
            }

            override fun onStartSlideHorizontal() {
                mAdjustProgressLayout.setDuration(mVideoPlayer.videoDuration)
                mAdjustProgressLayout.setCurrent(mVideoPlayer.currentPlayPosition)
                mAdjustProgressLayout.show()
            }

            override fun onSlidingHorizontal(delta: Float) {
                mAdjustProgressLayout.updateProgress(delta)
            }

            override fun onEndSlideHorizontal() {
                mAdjustProgressLayout.hide()
                mVideoPlayer.seekTo(mAdjustProgressLayout.getCurrent())
                if (mVideoPlayer.isPause || mVideoPlayer.isCompleted) {
                    start()
                }
            }
        })

        // 加载页面
        mVideoLoadingLayout.setBackListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onClickBack()
            }
        })

        // 加载失败关闭
        mVideoErrorLayout.setBackListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onClickBack()
            }
        })

        // 加载失败重试
        mVideoErrorLayout.setRetryListener(OnClickListener {
            reload()
        })
    }

    private fun initData() {
        val setting = IjkPlayerSetting.getDefault()
        setting.aspectRatioType = mAspectRatioType
        mVideoPlayer.init(setting)
    }

    /** 初始化MediaView */
    fun initMediaView(activity: Activity) {
        mActivity = activity
        mVideoLoadingLayout.show()
        mVideoLoadingLayout.showPlayerComplete()
    }

    /** 显示加载页 */
    fun showLoading() {
        mVideoLoadingLayout.show()
        mVideoErrorLayout.hide()
    }

    /** 设置视频名称[videoName] */
    fun setTitle(videoName: String) {
        mVideoTopMenuLayout.setTitle(videoName)
    }

    /** 设置播放路径[path] */
    fun setVideoPath(path: String) {
        mVideoPlayer.setVideoPath(path)
        mVideoLoadingLayout.showLoadUrlComplete()
        mVideoLoadingLayout.showStartAnalysisUrl()
    }

    /** 是否暂停 */
    fun isPause() = mVideoPlayer.isPause

    /** 是否正在播放 */
    fun isPlaying() = mVideoPlayer.isPlaying

    /** 开始播放 */
    fun start() {
        mVideoPlayer.start()
        mBottomMenuLayout.showPlayStatus()
        mBottomMenuLayout.startUpdateProgress()
        Log.v(MediaView.TAG, "开始播放")
    }

    /** 开始播放 */
    fun seekTo(position: Long) {
        mVideoPlayer.seekTo(position)
        Log.v(MediaView.TAG, "进度调整：$position")
    }

    /** 暂停 */
    fun pause() {
        mVideoPlayer.pause()
        mBottomMenuLayout.showPauseStatus()
        mBottomMenuLayout.stopUpdateProgress()
        Log.v(MediaView.TAG, "暂停")
    }

    /** 重头播放 */
    fun resume() {
        mVideoPlayer.resume()
        mBottomMenuLayout.showPlayStatus()
        mBottomMenuLayout.startUpdateProgress()
        Log.v(MediaView.TAG, "重播")
    }

    /** 重载 */
    fun reload() {
        mVideoLoadingLayout.show()
        mVideoLoadingLayout.showEnter()
        mVideoLoadingLayout.showStartAnalysisUrl()
        mVideoErrorLayout.hide()
        if (mBreakPosition > 0) {
            mVideoPlayer.seekTo(mBreakPosition)
        }
        resume()
    }

    /** 释放资源 */
    fun release() {
        if (isPlay) {
            mVideoPlayer.release()
        }
        stopAutoHideMenu()
        mVideoTopMenuLayout.release()
        mBottomMenuLayout.stopUpdateProgress()
        Log.v(MediaView.TAG, "释放资源")
    }

    /** 设置是否全屏[isFull] */
    fun setFullScreen(isFull: Boolean) {
        isFullScreen = isFull
        mSlideControlLayout.setScreenSize(isFull, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        mVideoTopMenuLayout.visibility = if (isFull) View.VISIBLE else View.GONE
        mVideoTopMenuLayout.setFullScreen(isFull)
        mBottomMenuLayout.setFullScreen(isFull)
        mVideoErrorLayout.setFullScreen(isFull)
        mVideoLoadingLayout.setFullScreen(isFull)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    /** 菜单是否显示 */
    private fun isMenuShow() = mVideoTopMenuLayout.isShow() || mBottomMenuLayout.isShow()

    /** 显示菜单 */
    private fun showMenu() {
        if (isFullScreen) {
            mVideoTopMenuLayout.show()
        }
        mBottomMenuLayout.show()
        updateAutoHideMenu()
    }

    /** 隐藏菜单 */
    private fun hideMenu() {
        if (isFullScreen) {
            mVideoTopMenuLayout.hide()
        }
        mBottomMenuLayout.hide()
    }

    /** 更新自动隐藏菜单 */
    private fun updateAutoHideMenu() {
        if (mVideoPlayer.isPause()) {// 暂停状态下不启用
            return
        }
        stopAutoHideMenu()
        mAutoHideMenuObserver = object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: Long) {
                hideMenu()
                stopAutoHideMenu()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {
            }
        }

        Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAutoHideMenuObserver!!)
        Log.v(MediaView.TAG, "开始自动隐藏菜单")
    }

    /** 停止自动隐藏菜单 */
    private fun stopAutoHideMenu() {
        if (mAutoHideMenuObserver != null) {
            if (mDisposable != null) {
                mDisposable!!.dispose()
                mDisposable = null
            }
            mAutoHideMenuObserver = null
            Log.v(MediaView.TAG, "停止自动隐藏菜单")
        }
    }

    /** 显示设置菜单 */
    private fun showSettingDialog() {
        val dialog = VideoSettingDialog(context)
        dialog.init(mPlayType, mAspectRatioType)
        dialog.setListener(object : VideoSettingDialog.Listener {
            override fun onPlayTypeChanged(playType: Int) {
                mPlayType = playType
            }

            override fun onAspectRatioChanged(aspectRatioType: Int) {
                if (mAspectRatioType != aspectRatioType) {
                    mVideoPlayer.setAspectRatio(aspectRatioType)
                    mAspectRatioType = aspectRatioType
                }
            }

            override fun onCancel(dialog: DialogInterface) {
                showMenu()
            }
        })
        dialog.show()
        hideMenu()
        stopAutoHideMenu()
    }

    /** 处理播放完成 */
    private fun handleCompletion(@Constant.PlayType playType: Int) {
        if (playType == Constant.UN_NEXT){// 不连播
            return
        }
        if (playType == Constant.AUTO_NEXT){// 自动连播
            return
        }
        if (playType == Constant.SINGLE_CYCLE){// 单集循环
            resume()
            return
        }
        if (playType == Constant.EXIT_END){// 播放完退出全屏
            if (isFullScreen && mListener != null){
                mListener!!.onClickBack()
            }
            return
        }
    }

    interface Listener {
        /** 点击返回按钮 */
        fun onClickBack()

        /** 屏幕变化 */
        fun onScreenChange(isFull: Boolean)
    }
}