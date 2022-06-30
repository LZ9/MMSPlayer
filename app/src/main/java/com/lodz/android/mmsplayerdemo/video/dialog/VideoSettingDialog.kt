package com.lodz.android.mmsplayerdemo.video.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.widget.RadioGroup
import android.widget.TextView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.mmsplayer.ijk.media.IRenderView
import com.lodz.android.mmsplayer.ijk.setting.IjkPlayerSetting
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.config.Constant
import com.lodz.android.pandora.widget.dialog.BaseRightDialog

/**
 * 播放器设置弹框
 * Created by zhouL on 2018/10/31.
 */
class VideoSettingDialog(context: Context) : BaseRightDialog(context, R.style.NoDimDialog) {

    /** 播放类型 */
    private val mPlayTypeRg by bindView<RadioGroup>(R.id.play_type_rg)
    /** 宽高比 */
    private val mAspectRatioRg by bindView<RadioGroup>(R.id.aspect_ratio_rg)
    /** 倍数 */
    private val mSpeedRg by bindView<RadioGroup>(R.id.speed_rg)

    /** 监听器 */
    private var mListener: Listener? = null

    /** 播放类型 */
    @Constant.PlayType
    private var mPlayType: Int = Constant.UN_NEXT

    /** 倍数 */
    @Constant.PlayType
    private var mSpeedType: String = Constant.SPEED_1_0X

    /** 宽高比 */
    @IjkPlayerSetting.AspectRatioType
    private var mAspectRatioType: Int = IRenderView.AR_ASPECT_FIT_PARENT

    override fun getLayoutId(): Int = R.layout.dialog_video_setting

    override fun setListeners() {
        super.setListeners()
        mPlayTypeRg.setOnCheckedChangeListener { group, checkedId ->
            val playType = when (checkedId) {
                R.id.un_next_rb -> Constant.UN_NEXT
                R.id.auto_next_rb -> Constant.AUTO_NEXT
                R.id.single_cycle_rb -> Constant.SINGLE_CYCLE
                R.id.exit_end_rb -> Constant.EXIT_END
                else -> Constant.UN_NEXT
            }
            if (mListener != null){
                mListener!!.onPlayTypeChanged(playType)
            }
        }

        mAspectRatioRg.setOnCheckedChangeListener { group, checkedId ->
            val aspectRatioType = when (checkedId) {
                R.id.aspect_ratio_default_rb -> IRenderView.AR_ASPECT_FIT_PARENT
                R.id.aspect_ratio_full_rb -> IRenderView.AR_ASPECT_FILL_PARENT
                R.id.aspect_ratio_16_9_rb -> IRenderView.AR_16_9_FIT_PARENT
                R.id.aspect_ratio_4_3_rb -> IRenderView.AR_4_3_FIT_PARENT
                else -> IRenderView.AR_ASPECT_FIT_PARENT
            }
            if (mListener != null){
                mListener!!.onAspectRatioChanged(aspectRatioType)
            }
        }

        mSpeedRg.setOnCheckedChangeListener { group, checkedId ->
            val speedType = when (checkedId) {
                R.id.speed_05_rb -> Constant.SPEED_0_5X
                R.id.speed_075_rb -> Constant.SPEED_0_75X
                R.id.speed_10_rb -> Constant.SPEED_1_0X
                R.id.speed_125_rb -> Constant.SPEED_1_25X
                R.id.speed_15_rb -> Constant.SPEED_1_5X
                R.id.speed_20_rb -> Constant.SPEED_2_0X
                else -> Constant.SPEED_1_0X
            }
            if (mListener != null){
                mListener!!.onSpeedTypeChanged(speedType)
            }
        }

        setOnCancelListener { dialog ->
            if (mListener != null){
                mListener!!.onCancel(dialog)
            }
        }
    }

    override fun initData() {
        super.initData()
        setPlayType(mPlayType)
        setAspectRatio(mAspectRatioType)
        setSpeed(mSpeedType)
    }

    /** 初始化播放类型[playType]和宽高比[aspectRatioType] */
    fun init(@Constant.PlayType playType: Int, @IjkPlayerSetting.AspectRatioType aspectRatioType: Int, @Constant.SpeedType speedType: String){
        mPlayType = playType
        mAspectRatioType = aspectRatioType
        mSpeedType = speedType
    }

    /** 设置播放类型[playType] */
    private fun setPlayType(@Constant.PlayType playType: Int){
        when (playType) {
            Constant.AUTO_NEXT -> mPlayTypeRg.check(R.id.auto_next_rb)
            Constant.SINGLE_CYCLE -> mPlayTypeRg.check(R.id.single_cycle_rb)
            Constant.EXIT_END -> mPlayTypeRg.check(R.id.exit_end_rb)
            Constant.UN_NEXT -> mPlayTypeRg.check(R.id.un_next_rb)
            else -> mPlayTypeRg.check(R.id.un_next_rb)
        }
    }

    /** 设置宽高比[aspectRatioType] */
    private fun setAspectRatio(@IjkPlayerSetting.AspectRatioType aspectRatioType: Int){
        when (aspectRatioType) {
            IRenderView.AR_ASPECT_FILL_PARENT -> mAspectRatioRg.check(R.id.aspect_ratio_full_rb)
            IRenderView.AR_16_9_FIT_PARENT -> mAspectRatioRg.check(R.id.aspect_ratio_16_9_rb)
            IRenderView.AR_4_3_FIT_PARENT -> mAspectRatioRg.check(R.id.aspect_ratio_4_3_rb)
            IRenderView.AR_ASPECT_FIT_PARENT -> mAspectRatioRg.check(R.id.aspect_ratio_default_rb)
            else -> mAspectRatioRg.check(R.id.aspect_ratio_default_rb)
        }
    }

    /** 设置倍数[speedType] */
    private fun setSpeed(@Constant.SpeedType speedType: String) {
        when (speedType) {
            Constant.SPEED_0_5X -> mSpeedRg.check(R.id.speed_05_rb)
            Constant.SPEED_0_75X -> mSpeedRg.check(R.id.speed_075_rb)
            Constant.SPEED_1_0X -> mSpeedRg.check(R.id.speed_10_rb)
            Constant.SPEED_1_25X -> mSpeedRg.check(R.id.speed_125_rb)
            Constant.SPEED_1_5X -> mSpeedRg.check(R.id.speed_15_rb)
            Constant.SPEED_2_0X -> mSpeedRg.check(R.id.speed_20_rb)
            else -> mSpeedRg.check(R.id.speed_10_rb)
        }
    }

    /** 设置监听器 */
    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        /** 播放类型改变 */
        fun onPlayTypeChanged(@Constant.PlayType playType: Int)

        /** 宽高比改变 */
        fun onAspectRatioChanged(@IjkPlayerSetting.AspectRatioType aspectRatioType: Int)

        /** 倍数改变 */
        fun onSpeedTypeChanged(@Constant.SpeedType speedType: String)

        /** 取消弹框 */
        fun onCancel(dialog: DialogInterface)
    }

}