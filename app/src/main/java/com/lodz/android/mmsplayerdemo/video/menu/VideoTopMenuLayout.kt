package com.lodz.android.mmsplayerdemo.video.menu

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lodz.android.core.network.NetInfo
import com.lodz.android.core.network.NetworkManager
import com.lodz.android.core.utils.AnimUtils
import com.lodz.android.core.utils.DateUtils
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.video.view.MediaView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 顶部菜单
 * Created by zhouL on 2018/10/23.
 */
class VideoTopMenuLayout : LinearLayout {

    /** 返回按钮 */
    private val mBackBtn by lazy {
        findViewById<ImageView>(R.id.back_btn)
    }
    /** 标题栏 */
    private val mTitleTv by lazy {
        findViewById<TextView>(R.id.title_tv)
    }
    /** 信息栏 */
    private val mInfoLayout by lazy {
        findViewById<ViewGroup>(R.id.info_layout)
    }
    /** 时间 */
    private val mTimeTv by lazy {
        findViewById<TextView>(R.id.time_tv)
    }
    /** 网络状态 */
    private val mNetworkTypeTv by lazy {
        findViewById<TextView>(R.id.network_type_tv)
    }
    /** 电量图标 */
    private val mBatteryImg by lazy {
        findViewById<ImageView>(R.id.battery_img)
    }

    /** 返回按钮监听器 */
    private var mBackListener: View.OnClickListener? = null
    /** 是否全屏模式 */
    private var isFullScreen = false
    /** 时间更新观察者 */
    private var mUpdateTimeObserver: Observer<Long>? = null
    /** 订阅者 */
    private var mDisposable: Disposable? = null
    /** 电池电量接收器 */
    private val mBatteryBroadcastReceiver = BatteryBroadcastReceiver()


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    )

    /** 网络状态变化监听器 */
    private val mNetworkListener = object : NetworkManager.NetworkListener {
        override fun onNetworkStatusChanged(isNetworkAvailable: Boolean, netInfo: NetInfo?) {
            updateNetworkTypeText()// 更新网络状态提示
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_top_menu, this)
        setListeners()
        registerBattery()// 注册电量变化广播监听
        startUpdateTime()// 更新手机时间
        NetworkManager.get().addNetworkListener(mNetworkListener)// 注册网络监听器
        updateNetworkTypeText()// 更新网络状态
    }

    private fun setListeners() {
        mBackBtn.setOnClickListener {
            if (mBackListener != null) {
                mBackListener!!.onClick(it)
            }
        }
    }

    /** 设置是否全屏[isFull] */
    fun setFullScreen(isFull: Boolean) {
        isFullScreen = isFull
    }

    /** 设置标题资源id[strResId] */
    fun setTitle(@StringRes strResId: Int) {
        mTitleTv.setText(strResId)
    }

    /** 设置标题[title] */
    fun setTitle(title: String) {
        mTitleTv.text = title
    }

    /** 获取标题 */
    fun getTitle(): String = mTitleTv.text.toString()

    /** 设置返回按钮监听器 */
    fun setBackListener(listener: View.OnClickListener) {
        mBackListener = listener
    }

    /** 顶部菜单是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示顶部菜单 */
    fun show() {
        if (!isShow()) {
            AnimUtils.startAnim(context, this, if (isFullScreen) R.anim.anim_top_in else R.anim.anim_fade_in, View.VISIBLE)
        }
    }

    /** 隐藏顶部菜单 */
    fun hide() {
        if (isShow()) {
            AnimUtils.startAnim(context, this, if (isFullScreen) R.anim.anim_top_out else R.anim.anim_fade_out, View.GONE)
        }
    }

    /** 释放资源 */
    fun release() {
        unRegisterBattery()//解注册电量广播监听
        stopUpdateTime() //停止时间更新
        NetworkManager.get().removeNetworkListener(mNetworkListener)//移除
    }

    /** 注册电池广播接收器 */
    private fun registerBattery() {
        try {
            context.registerReceiver(mBatteryBroadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            mBatteryImg.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
            mBatteryImg.visibility = View.GONE
        }
    }

    /** 解注册电池广播接收器*/
    private fun unRegisterBattery() {
        try {
            context.unregisterReceiver(mBatteryBroadcastReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 开始更新时间 */
    private fun startUpdateTime() {
        stopUpdateTime()
        mUpdateTimeObserver = object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: Long) {
                updateTime()
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }
        Observable.interval(0, 60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mUpdateTimeObserver!!)

    }

    /** 停止更新时间 */
    private fun stopUpdateTime() {
        if (mUpdateTimeObserver != null) {
            if (mDisposable != null) {
                mDisposable!!.dispose()
                mDisposable = null
            }
            mUpdateTimeObserver = null
        }
    }

    /** 更新时间 */
    private fun updateTime() {
        val date = DateUtils.getCurrentFormatString(DateUtils.TYPE_1)
        mTimeTv.text = date
        Log.i(MediaView.TAG, "更新时间：${mTimeTv.text}")
    }

    /** 更新网路类型提示语 */
    private fun updateNetworkTypeText() {
        mNetworkTypeTv.text = getNetworkTextByType()
        Log.i(MediaView.TAG, "网络状态更新：${mNetworkTypeTv.text}")
    }

    /** 根据网络类型获取对应的文字 */
    private fun getNetworkTextByType(): String = when (NetworkManager.get().netType) {
        NetInfo.NETWORK_TYPE_NONE -> context.getString(R.string.video_network_type_offline)
        NetInfo.NETWORK_TYPE_WIFI -> context.getString(R.string.video_network_type_wifi)
        NetInfo.NETWORK_TYPE_4G -> context.getString(R.string.video_network_type_4g)
        NetInfo.NETWORK_TYPE_3G -> context.getString(R.string.video_network_type_3g)
        NetInfo.NETWORK_TYPE_2G -> context.getString(R.string.video_network_type_2g)
        else -> context.getString(R.string.video_network_type_unknown)
    }

    /** 电池电量接收器 */
    private inner class BatteryBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }
            val action = intent.action
            if (action.isNullOrEmpty()) {
                return
            }
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                val phoneBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) //电池剩余容量
                val maxBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100) //电池最大值。通常为100
                Log.i(MediaView.TAG, "手机当前电量 ---> $phoneBattery")
                Log.i(MediaView.TAG, "手机最大电量 ---> $maxBattery")
                val battery = phoneBattery * 100 / maxBattery
                mBatteryImg.setImageResource(getBatteryResImgId(battery))
            }
        }

        /** 根据电量[battery]获取图片资源id */
        @DrawableRes
        private fun getBatteryResImgId(battery: Int): Int {
            if (battery <= 10) {
                return R.drawable.ic_battery_1
            }
            if (battery <= 20) {
                return R.drawable.ic_battery_2
            }
            if (battery <= 30) {
                return R.drawable.ic_battery_3
            }
            if (battery <= 40) {
                return R.drawable.ic_battery_4
            }
            if (battery <= 50) {
                return R.drawable.ic_battery_5
            }
            if (battery <= 60) {
                return R.drawable.ic_battery_6
            }
            if (battery <= 70) {
                return R.drawable.ic_battery_7
            }
            if (battery <= 80) {
                return R.drawable.ic_battery_8
            }
            if (battery < 100) {
                return R.drawable.ic_battery_9
            }
            return R.drawable.ic_battery_10
        }
    }

}