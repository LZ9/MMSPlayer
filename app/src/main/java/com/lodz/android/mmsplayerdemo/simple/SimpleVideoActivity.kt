package com.lodz.android.mmsplayerdemo.simple

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.mmsplayer.contract.IVideoPlayer
import com.lodz.android.mmsplayer.impl.MmsVideoView
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.pandora.base.activity.AbsActivity
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * 基础播放器界面
 * Created by zhouL on 2018/10/19.
 */
class SimpleVideoActivity : AbsActivity() {

    companion object {

        private const val EXTRA_VIDEO_PATH = "extra_video_path"

        fun start(context: Context, uri: Uri) {
            val intent = Intent(context, SimpleVideoActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_PATH, uri)
            context.startActivity(intent)
        }
    }

    private val mVideoPlayer: IVideoPlayer by bindView<MmsVideoView>(R.id.video_view)

    override fun getAbsLayoutId(): Int = R.layout.activity_simple_video

    override fun setListeners() {
        super.setListeners()
        mVideoPlayer.setListener(object :MmsVideoView.Listener{
            override fun onPrepared() {
                toastShort(R.string.simple_prepared)
            }

            override fun onBufferingStart() {
                toastShort(R.string.simple_buffering_start)
            }

            override fun onBufferingEnd() {
                toastShort(R.string.simple_buffering_end)
            }

            override fun onCompletion() {
                toastShort(R.string.simple_completion)
            }

            override fun onError(errorType: Int, msg: String?) {
                toastShort(getString(R.string.simple_error, msg))
            }

            override fun onMediaPlayerCreated(mediaPlayer: IMediaPlayer) {
                // do something
            }
        })
    }

    override fun initData() {
        super.initData()
        val uri = intent.getParcelableExtra<Uri>(EXTRA_VIDEO_PATH)
        if (uri == null){
            toastShort("获取路径失败")
            return
        }
        mVideoPlayer.init()
        mVideoPlayer.setVideoURI(uri)
        mVideoPlayer.start()
    }

    override fun finish() {
        mVideoPlayer.release()
        super.finish()
    }
}