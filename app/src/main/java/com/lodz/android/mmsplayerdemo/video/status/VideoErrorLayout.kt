package com.lodz.android.mmsplayerdemo.video.status

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lodz.android.core.utils.AnimUtils
import com.lodz.android.mmsplayerdemo.R

/**
 * 播放失败页面
 * Created by zhouL on 2018/10/23.
 */
class VideoErrorLayout : LinearLayout {

    /** 返回按钮 */
    private val mBackBtn by lazy {
        findViewById<ImageView>(R.id.back_btn)
    }

    /** 重试按钮 */
    private val mRetryBtn by lazy {
        findViewById<TextView>(R.id.retry_btn)
    }

    /** 返回按钮监听器 */
    private var mBackListener: View.OnClickListener? = null
    /** 重试按钮监听器 */
    private var mRetryListener: View.OnClickListener? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_error, this)
        setListeners()
    }

    private fun setListeners() {
        mBackBtn.setOnClickListener {
            if (mBackListener != null) {
                mBackListener!!.onClick(it)
            }
        }

        mRetryBtn.setOnClickListener {
            if (mRetryListener != null) {
                mRetryListener!!.onClick(it)
            }
        }
    }

    /** 设置返回按钮监听器 */
    fun setBackListener(listener: View.OnClickListener) {
        mBackListener = listener
    }

    /** 设置重试按钮监听器 */
    fun setRetryListener(listener: View.OnClickListener) {
        mRetryListener = listener
    }

    /** 播放失败页是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示播放失败页 */
    fun show() {
        if (!isShow()) {
            AnimUtils.startAnim(context, this, R.anim.anim_fade_in, View.VISIBLE)
        }
    }

    /** 隐藏播放失败页 */
    fun hide() {
        if (isShow()) {
            visibility = View.GONE
        }
    }
}