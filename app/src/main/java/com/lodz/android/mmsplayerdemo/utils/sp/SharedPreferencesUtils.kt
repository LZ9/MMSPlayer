package com.lodz.android.mmsplayerdemo.utils.sp

import android.content.Context
import com.lodz.android.mmsplayerdemo.App

/**
 * SP帮助类
 * Created by zhouL on 2018/10/22.
 */
object SharedPreferencesUtils {

    /** 保存String类型数据，键[key]，值[value] */
    fun putString(key: String, value: String) {
        val context = App.get()
        if (context == null) {
            return
        }
        val editor = context.getSharedPreferences(SpConfig.SP_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    /** 保存Int类型数据，键[key]，值[value] */
    fun putInt(key: String, value: Int) {
        val context = App.get()
        if (context == null) {
            return
        }
        val editor = context.getSharedPreferences(SpConfig.SP_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /** 获取Int型数据，键[key]，默认值[defValue] */
    fun getInt(key: String, defValue: Int): Int {
        val context = App.get()
        if (context == null) {
            return defValue
        }
        val sp = context.getSharedPreferences(SpConfig.SP_NAME, Context.MODE_PRIVATE)
        try {
            return sp.getInt(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /** 获取String型数据，键[key]，默认值[defValue] */
    fun getString(key: String, defValue: String): String {
        val context = App.get()
        if (context == null) {
            return defValue
        }
        val sp = context.getSharedPreferences(SpConfig.SP_NAME, Context.MODE_PRIVATE)
        try {
            return sp.getString(key, defValue) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }
}