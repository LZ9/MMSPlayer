package com.lodz.android.mmsplayerdemo.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * 屏幕帮助类
 * Created by zhouL on 2018/10/22.
 */
object ScreenUtils {

    /** 获得屏幕宽度 */
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }


    /** 获得屏幕高度 */
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }
}