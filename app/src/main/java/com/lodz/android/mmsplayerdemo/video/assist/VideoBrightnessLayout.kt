package com.lodz.android.mmsplayerdemo.video.assist

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.mmsplayerdemo.R

/**
 * 亮度页面
 * Created by zhouL on 2018/10/29.
 */
class VideoBrightnessLayout : LinearLayout {

    /** 亮度进度条 */
    private val mBrightnessProgressBar by bindView<ProgressBar>(R.id.brightness_pb)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_brightness, this)
    }

    /** 亮度界面是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示亮度界面 */
    fun show() {
        visibility = View.VISIBLE
    }

    /** 隐藏亮度界面 */
    fun hide() {
        visibility = View.GONE
    }

    /** 更新亮度，更新界面[activity]，进度更新参数[delta] */
    fun updateBrightness(activity: Activity, delta: Float) {
        val layoutParams = activity.window.attributes
        var current = layoutParams.screenBrightness//获取当前亮度
        if (current == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {// 如果亮度是-1则设置默认值为0.5
            current = 0.5f
        }
        current += delta * WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        if (current >= WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL) {
            current = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        }
        if (current <= WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF) {
            current = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
        }
        setBrightnessProgress(current)//设置亮度
        layoutParams.screenBrightness = current
        activity.window.attributes = layoutParams
    }

    /** 设置亮度值[brightness] */
    private fun setBrightnessProgress(brightness: Float) {
        val progress = brightness * 100
        mBrightnessProgressBar.progress = progress.toInt()
    }


}