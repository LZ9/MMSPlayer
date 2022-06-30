package com.lodz.android.mmsplayerdemo.config

import androidx.annotation.IntDef
import androidx.annotation.StringDef

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


    /** 0.5X */
    const val SPEED_0_5X = "0.5"
    /** 0.75X */
    const val SPEED_0_75X = "0.75"
    /** 1.0X */
    const val SPEED_1_0X = "1.0"
    /** 1.25X */
    const val SPEED_1_25X = "1.25"
    /** 1.5X */
    const val SPEED_1_5X = "1.5"
    /** 2.0X */
    const val SPEED_2_0X = "2.0"

    @StringDef(SPEED_0_5X, SPEED_0_75X, SPEED_1_0X, SPEED_1_25X, SPEED_1_5X, SPEED_2_0X)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SpeedType

}