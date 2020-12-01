package com.lodz.android.mmsplayerdemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lodz.android.core.network.NetworkManager

/**
 * Application
 * Created by zhouL on 2018/10/22.
 */
class App :Application(){
    companion object {
        private var sInstance: App? = null
        fun get() = sInstance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        NetworkManager.get().init(this)
    }

}