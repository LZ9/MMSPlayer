package com.lodz.android.mmsplayerdemo

import android.os.Bundle
import android.widget.Button
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.mmsplayerdemo.simple.SimpleVideoActivity
import com.lodz.android.mmsplayerdemo.widget.VideoActivity
import com.lodz.android.pandora.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    private val mSimpleBtn by bindView<Button>(R.id.simple_btn)

    private val mWidgetBtn by bindView<Button>(R.id.widget_btn)

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.app_name)
        getTitleBarLayout().setBackgroundColor(getColorCompat(R.color.colorPrimary))
    }

    override fun setListeners() {
        super.setListeners()
        getTitleBarLayout().setOnBackBtnClickListener {
            finish()
        }

        mSimpleBtn.setOnClickListener {
            SimpleVideoActivity.start(this)
        }

        mWidgetBtn.setOnClickListener {
            VideoActivity.start(this, getString(R.string.info_name), "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}
