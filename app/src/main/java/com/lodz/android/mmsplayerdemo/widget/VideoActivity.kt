package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lodz.android.core.network.NetworkManager
import com.lodz.android.core.utils.ToastUtils
import com.lodz.android.mmsplayerdemo.R
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

            if (!NetworkManager.get().isWifi()) {// 非wifi下提示用户

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }
}