package com.lodz.android.mmsplayerdemo.video.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import com.lodz.android.mmsplayer.contract.IVideoPlayer
import com.lodz.android.mmsplayer.impl.MmsVideoView
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.utils.sp.SpManager
import com.lodz.android.mmsplayerdemo.video.menu.SlideControlLayout
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
//    /** 底部菜单 */
//    private val mBottomMenuLayout by lazy {
//        findViewById<VideoBottomMenuLayout>(R.id.bottom_menu_layout)
//    }

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
                mVideoLoadingLayout.showAnalysisUrlComplete()
//                mBottomMenuLayout.initConfig(mVideoPlayer.currentPlayPosition, mVideoPlayer.videoDuration)


                mVideoLoadingLayout.hide()
            }

            override fun onBufferingStart() {
            }

            override fun onBufferingEnd() {
            }

            override fun onCompletion() {
//                mBottomMenuLayout.showPauseStatus()
//                mBottomMenuLayout.stopUpdateProgress()
//                mBottomMenuLayout.setPlayCompletion()
            }

            override fun onError(errorType: Int, msg: String?) {
                if (mVideoErrorLayout.isShow()) {// 如果播放错误页已经显示则不再重复处理
                    return
                }
                mVideoErrorLayout.show()
                mVideoLoadingLayout.showAnalysisError()
                mVideoLoadingLayout.hide()
            }

        })

        // 顶部菜单
        mVideoTopMenuLayout.setBackListener(OnClickListener {
            if (mListener != null) {
                mListener!!.onClickBack()
            }
        })

        // 底部菜单
//        mBottomMenuLayout.setListener(object : VideoBottomMenuLayout.Listener {
//            override fun onClickPlay() {
//                start()
//            }
//
//            override fun onClickPause() {
//                pause()
//            }
//
//            override fun onClickSetting() {
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//            }
//
//            override fun onSeekChangedFromUser(position: Long, duration: Long) {
//                mVideoPlayer.seekTo(position)
//                if (mVideoPlayer.isPause || mVideoPlayer.isCompleted) {
//                    start()
//                }
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//            }
//
//            override fun getBufferPercentage(): Long = mVideoPlayer.bufferPercentage.toLong()
//
//            override fun getCurrentPlayPosition(): Long = mVideoPlayer.currentPlayPosition
//
//        })

        // 手势划动回调控件
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
        mVideoPlayer.init()
        mVideoPlayer.setAspectRatio(SpManager.get().getAspectRatioType()) // 设置宽高比
    }

    /** 初始化MediaView */
    fun initMediaView(activity: Activity) {
        mActivity = activity
        mVideoLoadingLayout.show()
        mVideoLoadingLayout.showPlayerComplete()
    }

    /** 显示加载页 */
    fun showLoading(){
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
    }

    /** 暂停 */
    fun pause() {
        mVideoPlayer.pause()
    }

    /** 重头播放 */
    fun resume() {
        mVideoPlayer.resume()
    }

    fun reload() {
        mVideoLoadingLayout.show()
        mVideoLoadingLayout.showEnter()
        mVideoLoadingLayout.showStartAnalysisUrl()
        mVideoErrorLayout.hide()
        resume()
    }

    /** 释放资源 */
    fun release() {
        if (isPlay) {
            mVideoPlayer.release()
        }
        mVideoTopMenuLayout.release()
    }

    fun setFullScreen(isFull: Boolean) {
        mSlideControlLayout.setScreenSize(isFull, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        mVideoTopMenuLayout.visibility = if (isFull) View.VISIBLE else View.GONE
        mVideoTopMenuLayout.setFullScreen(isFull)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    /** 菜单是否显示 */
    private fun isMenuShow() = mVideoTopMenuLayout.isShow()

    /** 显示菜单 */
    private fun showMenu() {
        mVideoTopMenuLayout.show()
    }

    /** 隐藏菜单 */
    private fun hideMenu() {
        mVideoTopMenuLayout.show()
    }

    /** 更新自动隐藏菜单 */
    private fun updateAutoHideMenu(){
        stopAutoHideMenu()
        mAutoHideMenuObserver = object :Observer<Long>{
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: Long) {
                hideMenu()
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
    }

    /** 停止自动隐藏菜单 */
    private fun stopAutoHideMenu(){
        if (mAutoHideMenuObserver != null) {
            if (mDisposable != null) {
                mDisposable!!.dispose()
                mDisposable = null
            }
            mAutoHideMenuObserver = null
        }
    }

    interface Listener {
        fun onClickBack()
    }
}