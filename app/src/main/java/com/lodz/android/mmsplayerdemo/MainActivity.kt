package com.lodz.android.mmsplayerdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lodz.android.mmsplayerdemo.simple.SimpleVideoActivity

class MainActivity : AppCompatActivity() {

    private val mSimpleBtn by lazy {
        findViewById<Button>(R.id.simple_btn)
    }

    private val mWidgetBtn by lazy {
        findViewById<Button>(R.id.widget_btn)
    }

    private val mHalfBtn by lazy {
        findViewById<Button>(R.id.half_btn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSimpleBtn.setOnClickListener {
            SimpleVideoActivity.start(this)
        }

        mWidgetBtn.setOnClickListener {
            Toast.makeText(applicationContext, R.string.main_develop, Toast.LENGTH_SHORT).show()
        }

        mHalfBtn.setOnClickListener {
            Toast.makeText(applicationContext, R.string.main_develop, Toast.LENGTH_SHORT).show()
        }
    }
}
