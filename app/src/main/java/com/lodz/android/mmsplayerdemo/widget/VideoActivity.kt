package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lodz.android.core.log.PrintLog
import com.lodz.android.core.network.NetworkManager
import com.lodz.android.core.utils.DensityUtils
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


    /** tab栏目名称 */
    private val TAB_NAMES = arrayOf("简介", "评论")

    /** 返回按钮 */
    private val mBackBtn by lazy {
        findViewById<ImageView>(R.id.back_btn)
    }
    /** 播放器布局 */
    private val mVideoLayout by lazy {
        findViewById<ViewGroup>(R.id.video_layout)
    }
    /** 播放器 */
    private val mMediaView by lazy {
        findViewById<MediaView>(R.id.media_view)
    }
    /** 数据流量播放提示页 */
    private val mVideoPhoneDataLayout by lazy {
        findViewById<VideoPhoneDataLayout>(R.id.video_phone_data_layout)
    }
    /** TabLayout */
    private val mTabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout)
    }
    /** ViewPager */
    private val mViewPager by lazy {
        findViewById<ViewPager>(R.id.view_pager)
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
        initViewPager()
        mBackBtn.bringToFront()
    }

    private fun setListeners() {
        mMediaView.setListener(object : MediaView.Listener {
            override fun onScreenChange(isFull: Boolean) {
                changeOrientation(isFull)
            }

            override fun onClickBack() {
                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {// 全屏时转半屏
                    changeOrientation(false)
                } else if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {//半屏退出
                    finish()
                }
            }
        })

        // 数据流量播放提示页关闭
        mVideoPhoneDataLayout.setBackListener(View.OnClickListener {
            finish()
        })

        // 数据流量播放提示页 流量播放
        mVideoPhoneDataLayout.setDataPlayListener(View.OnClickListener {
            mVideoPhoneDataLayout.hide()
            mMediaView.showLoading()
            mMediaView.reload()
        })

        mBackBtn.setOnClickListener {
            finish()
        }
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

        PrintLog.e(MediaView.TAG, "VideoName : $mVideoName")
        PrintLog.e(MediaView.TAG, "VideoPath : $mVideoPath")
    }

    /** 初始化ViewPager */
    private fun initViewPager() {
        mViewPager.offscreenPageLimit = TAB_NAMES.size
        mViewPager.adapter = TabAdapter(supportFragmentManager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    /** 设置全屏无状态栏 */
    private fun setFullWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)  //取消标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)  //取消状态栏
    }

    private fun initMediaView() {
        mMediaView.initMediaView(this)
        mMediaView.setTitle(mVideoName)
        mMediaView.setFullScreen(false)
        mMediaView.setVideoPath(mVideoPath)
        mVideoPhoneDataLayout.needBackBtn(false)
    }

    /** 改变屏幕方向，是否横屏[isLandscape] */
    private fun changeOrientation(isLandscape: Boolean) {
        mMediaView.setFullScreen(isLandscape)
        val lp = mVideoLayout.layoutParams
        lp.height = if (isLandscape) FrameLayout.LayoutParams.MATCH_PARENT else DensityUtils.dp2px(applicationContext, 200f)
        mVideoLayout.layoutParams = lp
        requestedOrientation = if (isLandscape) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mBackBtn.visibility = if (isLandscape) View.GONE else View.VISIBLE
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


    private inner class TabAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> InfoFragment.newInstance()
            1 -> CommentFragment.newInstance()
            else -> InfoFragment.newInstance()
        }

        override fun getCount(): Int = TAB_NAMES.size

        override fun getPageTitle(position: Int): CharSequence? = TAB_NAMES[position]

    }
}