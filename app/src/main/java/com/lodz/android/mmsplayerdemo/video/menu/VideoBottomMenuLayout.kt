package com.lodz.android.mmsplayerdemo.video.menu

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.lodz.android.mmsplayer.ijk.utils.MediaInfoUtils
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.utils.AnimUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 底部菜单
 * Created by zhouL on 2018/10/22.
 */
class VideoBottomMenuLayout : LinearLayout {

    /** 开始播放 */
    private val mPlayBtn by lazy {
        findViewById<ImageView>(R.id.play_btn)
    }
    /** 暂停播放 */
    private val mPauseBtn by lazy {
        findViewById<ImageView>(R.id.pause_btn)
    }
    /** 进度条 */
    private val mSeekBar by lazy {
        findViewById<SeekBar>(R.id.seek_bar)
    }
    /** 播放时间 */
    private val mPlayTimeTv by lazy {
        findViewById<TextView>(R.id.play_time_tv)
    }
    /** 设置按钮 */
    private val mSettingBtn by lazy {
        findViewById<ImageView>(R.id.setting_btn)
    }

    /** 监听器 */
    private var mListener: Listener? = null
    /** 进度更新观察者 */
    private var mUpdateProgressObserver: Observer<Long>? = null
    /** 订阅者 */
    private var mDisposable: Disposable? = null
    /** 总进度时间戳 */
    private var mDuration: Long = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_bottom_menu, this)
        setListeners()
    }

    private fun setListeners() {
        mPlayBtn.setOnClickListener {
            showPlayStatus() //进入播放状态
            if (mListener != null) {
                mListener!!.onClickPlay()
            }
        }

        mPauseBtn.setOnClickListener {
            showPauseStatus() //进入暂停状态
            if (mListener != null) {
                mListener!!.onClickPause()
            }
        }

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar == null) {
                    return
                }
                val position = progress2Position(progress, seekBar.max)
                if (fromUser) {
                    if (mListener != null) {
                        mListener!!.onSeekChangedFromUser(position, mDuration)
                    }
                }
                setPlayTime(position, mDuration)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (mListener != null && seekBar != null) {
                    mListener!!.onStartTrackingTouch(seekBar)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mListener != null && seekBar != null) {
                    mListener!!.onStopTrackingTouch(seekBar)
                }
            }
        })

        mSettingBtn.setOnClickListener {
            if (mListener != null) {
                mListener!!.onClickSetting()
            }
        }
    }

    /** 初始化配置，当前进度时间戳[current]，总进度时间戳[duration] */
    fun initConfig(current: Long, duration: Long) {
        mDuration = duration
        setPlayTime(current, duration)
        setProgress(current)
    }

    /** 设置当前进度时间戳[current] */
    fun setProgress(current: Long) {
        if (mDuration <= 0) {
            mSeekBar.progress = 0
            return
        }
        val progress = current * mSeekBar.max / mDuration
        if (progress <= 0) {
            mSeekBar.progress = 0
            return
        }

        if (progress >= mSeekBar.max) {
            mSeekBar.progress = mSeekBar.max
            return
        }
        mSeekBar.progress = progress.toInt()
    }

    /** 菜单是否显示 */
    fun isMenuShow() = visibility == View.VISIBLE

    /** 显示菜单 */
    fun showMenu() {
        if (!isMenuShow()) {
            AnimUtils.startAnim(context, this, R.anim.anim_bottom_in, View.VISIBLE)
        }
    }

    /** 隐藏菜单 */
    fun hideMenu() {
        if (isMenuShow()) {
            AnimUtils.startAnim(context, this, R.anim.anim_bottom_out, View.VISIBLE)
        }
    }

    /** 显示播放状态 */
    fun showPlayStatus() {
        //隐藏播放按钮显示暂停按钮
        mPlayBtn.visibility = View.GONE
        mPauseBtn.visibility = View.VISIBLE
    }

    /** 显示暂停状态 */
    fun showPauseStatus() {
        //隐藏暂停按钮显示播放按钮
        mPlayBtn.visibility = View.VISIBLE
        mPauseBtn.visibility = View.GONE
    }

    /** 开始更新进度 */
    fun startUpdateProgress() {
        stopUpdateProgress()
        mUpdateProgressObserver = object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: Long) {
                updateProgress()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {

            }
        }

        Observable.interval(0, 500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mUpdateProgressObserver!!)
    }

    /** 停止进度更新 */
    fun stopUpdateProgress() {
        if (mUpdateProgressObserver != null) {
            if (mDisposable != null) {
                mDisposable!!.dispose()
                mDisposable = null
            }
            mUpdateProgressObserver = null
        }
    }

    /** 设置播放完成 */
    fun setPlayCompletion() {
        setProgress(mDuration)
    }

    /** 设置监听器 */
    fun setListener(listener: Listener?) {
        mListener = listener
    }

    /** 更新进度 */
    private fun updateProgress() {
        setBufferPercentage(if (mListener != null) mListener!!.getBufferPercentage() else 0)
        setProgress(if (mListener != null) mListener!!.getCurrentPlayPosition() else 0)
    }

    /** 设置缓冲进度[bufferPercentage] */
    private fun setBufferPercentage(bufferPercentage: Long) {
        val secondProgress = bufferPercentage * mSeekBar.max / 100
        if (secondProgress <= 0) {
            mSeekBar.secondaryProgress = 0
            return
        }

        if (secondProgress >= mSeekBar.max) {
            mSeekBar.secondaryProgress = mSeekBar.max
            return
        }
        mSeekBar.secondaryProgress = secondProgress.toInt()
    }

    /** 设置播放时间，当前进度时间戳[current]，总进度时间戳[duration] */
    private fun setPlayTime(current: Long, duration: Long) {
        mPlayTimeTv.text = context.getString(
            R.string.video_play_progress,
            MediaInfoUtils.buildTimeMilli(current),
            MediaInfoUtils.buildTimeMilli(duration)
        )
    }

    /** 把进度条进度[progress]转换为播放进度，[max]为进度条总大小 */
    private fun progress2Position(progress: Int, max: Int): Long {
        val position = mDuration * progress / max
        if (position <= 0) {
            return 0
        }
        if (position >= mDuration) {
            return mDuration
        }
        return position
    }

    interface Listener {
        /** 点击播放按钮 */
        fun onClickPlay()

        /** 点击暂停按钮 */
        fun onClickPause()

        /** 点击设置按钮 */
        fun onClickSetting()

        /** 开始调整进度条 */
        fun onStartTrackingTouch(seekBar: SeekBar)

        /** 进度调整回调，[position]调整后进度，[duration]总进度 */
        fun onSeekChangedFromUser(position: Long, duration: Long)

        /** 结束进度条调整 */
        fun onStopTrackingTouch(seekBar: SeekBar)

        /** 获取缓存进度百分比 */
        fun getBufferPercentage(): Long

        /** 获取当前播放进度 */
        fun getCurrentPlayPosition(): Long
    }

}