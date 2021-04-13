package com.lodz.android.mmsplayerdemo

import android.content.Context
import androidx.multidex.MultiDex
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.pandora.base.application.BaseApplication

/**
 * Application
 * Created by zhouL on 2018/10/22.
 */
class App :BaseApplication(){
    companion object {
        private var sInstance: App? = null
        fun get() = sInstance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onStartCreate() {
        sInstance = this
        NetworkManager.get().init(this)
    }

    override fun onExit() {
    }
}