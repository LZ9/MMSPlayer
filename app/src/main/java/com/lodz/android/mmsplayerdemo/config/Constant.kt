package com.lodz.android.mmsplayerdemo.config

import androidx.annotation.IntDef

/**
 * 常量
 * Created by zhouL on 2018/10/31.
 */
object Constant {

    /** 不连播  */
    const val UN_NEXT = 0
    /** 自动连播  */
    const val AUTO_NEXT = 1
    /** 单集循环  */
    const val SINGLE_CYCLE = 2
    /** 播完退出  */
    const val EXIT_END = 3

    @IntDef(UN_NEXT, AUTO_NEXT, SINGLE_CYCLE, EXIT_END)
    @Retention(AnnotationRetention.SOURCE)
    annotation class PlayType

}