package com.lodz.android.mmsplayerdemo.video.assist

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.lodz.android.mmsplayerdemo.R

/**
 * 音量页面
 * Created by zhouL on 2018/10/29.
 */
class VideoVolumeLayout : LinearLayout {

    /** 音量进度条 */
    private val mVolumeProgressBar by lazy {
        findViewById<ProgressBar>(R.id.volume_pb)
    }

    /** 音量管理  */
    private var mAudioManager: AudioManager? = null
    /** 最大值  */
    private var mMaxVolume = 0
    /** 当前音量  */
    private var mCurrent = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_volume, this)
    }

    /** 音量界面是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示音量界面 */
    fun show() {
        visibility = View.VISIBLE
    }

    /** 隐藏音量界面 */
    fun hide() {
        visibility = View.GONE
    }

    /** 更新音量[delta] */
    fun updateVolume(activity: Activity, delta: Float) {
        if (mAudioManager == null) {
            configManager(activity)
        }

        if (mAudioManager != null) {
            mCurrent += delta * mMaxVolume
            if (mCurrent >= mMaxVolume) {
                mCurrent = mMaxVolume.toFloat()
            }
            if (mCurrent <= 0) {
                mCurrent = 0f
            }
            mVolumeProgressBar.progress = mCurrent.toInt()
            mAudioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrent.toInt(), 0)// 设置手机音量
        }
    }

    private fun configManager(activity: Activity) {
        mAudioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        if (mAudioManager == null) {
            return
        }
        mMaxVolume = mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)// 获取音量最大值
        mVolumeProgressBar.max = mMaxVolume
        mCurrent = mAudioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()// 获取当前音量值
    }


}