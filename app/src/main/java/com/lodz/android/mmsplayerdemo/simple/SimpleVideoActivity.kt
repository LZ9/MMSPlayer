package com.lodz.android.mmsplayerdemo.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lodz.android.mmsplayer.contract.IVideoPlayer
import com.lodz.android.mmsplayer.impl.MmsVideoView
import com.lodz.android.mmsplayerdemo.R

/**
 * 基础播放器界面
 * Created by zhouL on 2018/10/19.
 */
class SimpleVideoActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, SimpleVideoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mVideoPlayer : IVideoPlayer by lazy {
        findViewById<MmsVideoView>(R.id.video_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_video)

        mVideoPlayer.setListener(object : MmsVideoView.Listener{
            override fun onPrepared() {
                Toast.makeText(applicationContext, R.string.simple_prepared, Toast.LENGTH_SHORT).show()
            }

            override fun onBufferingStart() {
                Toast.makeText(applicationContext, R.string.simple_buffering_start, Toast.LENGTH_SHORT).show()
            }

            override fun onBufferingEnd() {
                Toast.makeText(applicationContext, R.string.simple_buffering_end, Toast.LENGTH_SHORT).show()
            }

            override fun onCompletion() {
                Toast.makeText(applicationContext, R.string.simple_completion, Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorType: Int, msg: String?) {
                Toast.makeText(applicationContext, getString(R.string.simple_error, msg), Toast.LENGTH_SHORT).show()
            }

        })
        mVideoPlayer.init()
        mVideoPlayer.setVideoPath("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
        mVideoPlayer.start()
    }

    override fun finish() {
        mVideoPlayer.release()
        super.finish()
    }
}