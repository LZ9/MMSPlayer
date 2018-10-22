package com.lodz.android.mmsplayerdemo.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

/**
 * 动画帮助类
 * Created by zhouL on 2018/10/22.
 */
object AnimUtils {

    fun startAnim(context: Context, view: View, @AnimRes animResId: Int, visibility: Int) {
        view.clearAnimation()
        val animation = AnimationUtils.loadAnimation(context, animResId)
        view.startAnimation(animation)
        view.visibility = visibility
    }

}