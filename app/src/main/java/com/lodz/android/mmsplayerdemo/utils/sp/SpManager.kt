package com.lodz.android.mmsplayerdemo.utils.sp

import com.lodz.android.mmsplayer.ijk.setting.IjkPlayerSetting

/**
 * SharedPreferences管理类
 * Created by zhouL on 2018/10/22.
 */
class SpManager private constructor() {

    companion object {
        private val sInstance = SpManager()
        fun get() = sInstance
    }

//    /**
//     * 设置播放结束类型
//     * @param playEndType 播放结束类型
//     */
//    fun setVideoPlayEndType(@VideoSettingDialog.PlayType playEndType: Int) {
//        SharedPreferencesUtils.putInt(SpConfig.VIDEO_PLAY_END_TYPE_KEY, playEndType)
//    }
//
//    /** 获取播放结束类型  */
//    fun getVideoPlayEndType(): Int {
//        return SharedPreferencesUtils.getInt(SpConfig.VIDEO_PLAY_END_TYPE_KEY, VideoSettingDialog.UN_NEXT)
//    }
//
    /**
     * 设置播放器宽高比类型
     * @param aspectRatioType 播放器宽高比类型
     */
    fun setAspectRatioType(@IjkPlayerSetting.AspectRatioType aspectRatioType: Int) {
        SharedPreferencesUtils.putInt(SpConfig.VIDEO_ASPECT_RATIO_TYPE_KEY, aspectRatioType)
    }

    /** 获取播放器宽高比类型  */
    @IjkPlayerSetting.AspectRatioType
    fun getAspectRatioType(): Int {
        return SharedPreferencesUtils.getInt(SpConfig.VIDEO_ASPECT_RATIO_TYPE_KEY, IjkPlayerSetting.getDefault().aspectRatioType)
    }


}