package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lodz.android.core.log.PrintLog
import com.lodz.android.core.network.NetworkManager
import com.lodz.android.core.utils.ToastUtils
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.video.status.VideoPhoneDataLayout
import com.lodz.android.mmsplayerdemo.video.view.MediaView

/**
 * 视频播放activity
 * Created by zhouL on 2018/10/22.
 */
class VideoActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_VIDEO_NAME = "extra_video_name"

        private const val EXTRA_VIDEO_PATH = "extra_video_path"

        fun start(context: Context, videoName: String, path: String) {
            if (!NetworkManager.get().isNetworkAvailable) {// 网络未连接时直接提示用户
                ToastUtils.showShort(context, R.string.network_no_connect)
                return
            }

            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_NAME, videoName)
            intent.putExtra(EXTRA_VIDEO_PATH, path)
            context.startActivity(intent)
        }
    }

    private val mMediaView by lazy {
        findViewById<MediaView>(R.id.media_view)
    }

    /** 数据流量播放提示页 */
    private val mVideoPhoneDataLayout by lazy {
        findViewById<VideoPhoneDataLayout>(R.id.video_phone_data_layout)
    }

    /** 视频路径 */
    private lateinit var mVideoPath: String
    /** 视频名称 */
    private lateinit var mVideoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullWindow()
        getParameter(intent)
        setContentView(R.layout.activity_video)
        initMediaView()
        setListeners()
        playVideo()
    }

    private fun setListeners() {
        mMediaView.setListener(object : MediaView.Listener {
            override fun onClickBack() {
                finish()
            }
        })

        // 数据流量播放提示页关闭
        mVideoPhoneDataLayout.setBackListener(View.OnClickListener {
            finish()
        })

        // 数据流量播放提示页 流量播放
        mVideoPhoneDataLayout.setDataPlayListener(View.OnClickListener {
            mVideoPhoneDataLayout.hide()
            mMediaView.start()
        })
    }

    /** 获取播放参数 */
    private fun getParameter(intent: Intent?) {
        if (intent == null) {
            ToastUtils.showShort(this, R.string.video_parameter_error)
            finish()
            return
        }
        mVideoPath = intent.getStringExtra(EXTRA_VIDEO_PATH)
        mVideoName = intent.getStringExtra(EXTRA_VIDEO_NAME)

        PrintLog.i(MediaView.TAG, "VideoName : $mVideoName")
        PrintLog.e(MediaView.TAG, "VideoPath : $mVideoPath")
    }

    /** 设置全屏无状态栏 */
    fun setFullWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)  //取消标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)  //取消状态栏
    }

    fun initMediaView() {
        mMediaView.initMediaView(this)
        mMediaView.setTitle(mVideoName)
        mMediaView.setFullScreen(true)
        mMediaView.setVideoPath(mVideoPath)
    }

    /** 播放视频 */
    private fun playVideo() {
        if (!NetworkManager.get().isWifi) {// 流量下
            mVideoPhoneDataLayout.show()
        } else {
            mVideoPhoneDataLayout.hide()
            mMediaView.start()
        }

    }

    override fun onPause() {
        super.onPause()
        if (mMediaView.isPlaying()) {
            mMediaView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mMediaView.isPause()) {
            mMediaView.start()
        }
    }

    override fun finish() {
        mMediaView.pause()
        mMediaView.release()
        super.finish()
    }


}