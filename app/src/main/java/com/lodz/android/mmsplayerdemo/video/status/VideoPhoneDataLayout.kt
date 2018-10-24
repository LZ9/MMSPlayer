package com.lodz.android.mmsplayerdemo.video.status

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.lodz.android.mmsplayerdemo.R

/**
 * 数据流量播放提示页
 * Created by zhouL on 2018/10/23.
 */
class VideoPhoneDataLayout : LinearLayout {

    /** 返回按钮 */
    private val mBackBtn by lazy {
        findViewById<ImageView>(R.id.back_btn)
    }

    /** 流量播放按钮 */
    private val mPlayBtn by lazy {
        findViewById<ViewGroup>(R.id.play_btn)
    }

    /** 返回按钮监听器 */
    private var mBackListener: View.OnClickListener? = null
    /** 流量播放按钮监听器 */
    private var mDataPlayListener: View.OnClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_phone_data, this)
        setListeners()
    }

    private fun setListeners() {
        mBackBtn.setOnClickListener {
            if (mBackListener != null) {
                mBackListener!!.onClick(it)
            }
        }

        mPlayBtn.setOnClickListener {
            if (mDataPlayListener != null) {
                mDataPlayListener!!.onClick(it)
            }
        }
    }

    /** 是否需要返回按钮[isNeed] */
    fun needBackBtn(isNeed: Boolean) {
        mBackBtn.visibility = if (isNeed) View.VISIBLE else View.GONE
    }

    /** 设置返回按钮监听器 */
    fun setBackListener(listener: View.OnClickListener) {
        mBackListener = listener
    }

    /** 设置流量播放按钮监听器 */
    fun setDataPlayListener(listener: View.OnClickListener) {
        mDataPlayListener = listener
    }

    /** 播放失败页是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示播放失败页 */
    fun show() {
        if (!isShow()) {
            visibility = View.VISIBLE
        }
    }

    /** 隐藏播放失败页 */
    fun hide() {
        if (isShow()) {
            visibility = View.GONE
        }
    }
}