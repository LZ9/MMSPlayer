package com.lodz.android.mmsplayerdemo.video.assist

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lodz.android.mmsplayer.ijk.utils.MediaInfoUtils
import com.lodz.android.mmsplayerdemo.R

/**
 * 进度调整页面
 * Created by zhouL on 2018/10/26.
 */
class VideoAdjustProgressLayout : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /** 进度 */
    private val mProgressTv by lazy {
        findViewById<TextView>(R.id.progress_tv)
    }

    /** 总进度  */
    private var mDuration: Long = 0
    /** 当前进度  */
    private var mCurrent: Long = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_adjust_progress, this)
    }

    /** 进度调整界面是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示进度调整界面 */
    fun show() {
        visibility = View.VISIBLE
    }

    /** 隐藏进度调整界面 */
    fun hide() {
        visibility = View.GONE
    }

    /** 设置总进度[duration] */
    fun setDuration(duration: Long) {
        mDuration = duration
    }

    /** 设置当前进度[current] */
    fun setCurrent(current: Long) {
        mCurrent = current
    }

    /** 更新进度，进度更新参数[delta] */
    fun updateProgress(delta: Float) {
        if (mDuration <= 0) {
            return
        }
        mCurrent += (mDuration * delta).toLong()// 计算当前进度
        if (mCurrent <= 0) {
            mCurrent = 0
        }
        if (mCurrent >= mDuration) {
            mCurrent = mDuration
        }
        mProgressTv.text = context.getString(R.string.video_play_progress, MediaInfoUtils.buildTimeMilli(mCurrent), MediaInfoUtils.buildTimeMilli(mDuration))
    }

    /** 获取当前进度 */
    fun getCurrent(): Long = mCurrent
}