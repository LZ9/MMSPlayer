package com.lodz.android.mmsplayerdemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lodz.android.component.widget.base.TitleBarLayout
import com.lodz.android.mmsplayerdemo.simple.SimpleVideoActivity
import com.lodz.android.mmsplayerdemo.widget.VideoActivity

class MainActivity : AppCompatActivity() {

    private val mTitleBarLayout by lazy {
        findViewById<TitleBarLayout>(R.id.title_bar_layout)
    }

    private val mSimpleBtn by lazy {
        findViewById<Button>(R.id.simple_btn)
    }

    private val mWidgetBtn by lazy {
        findViewById<Button>(R.id.widget_btn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mSimpleBtn.setOnClickListener {
            SimpleVideoActivity.start(this)
        }

        mWidgetBtn.setOnClickListener {
            VideoActivity.start(this, getString(R.string.info_name), "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
        }
    }


}
