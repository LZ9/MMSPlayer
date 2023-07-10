package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.intentExtrasNoNull
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.mmsplayerdemo.R
import com.lodz.android.mmsplayerdemo.databinding.ActivityVideoBinding
import com.lodz.android.mmsplayerdemo.video.view.MediaView
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleVp2Adapter

/**
 * 视频播放activity
 * Created by zhouL on 2018/10/22.
 */
class VideoActivity : AbsActivity() {

    companion object {

        private const val EXTRA_VIDEO_NAME = "extra_video_name"

        private const val EXTRA_VIDEO_PATH = "extra_video_path"

        /** tab栏目名称 */
        private val TAB_NAMES = arrayOf("简介", "评论")

        fun start(context: Context, videoName: String, path: String) {
            if (!NetworkManager.get().isNetworkAvailable()) {// 网络未连接时直接提示用户
                context.toastShort(R.string.network_no_connect)
                return
            }
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_NAME, videoName)
            intent.putExtra(EXTRA_VIDEO_PATH, path)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityVideoBinding by bindingLayout(ActivityVideoBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    /** 播放器 */
    private lateinit var mMediaView: MediaView

    /** 视频路径 */
    private val mVideoPath by intentExtrasNoNull(EXTRA_VIDEO_PATH, "")
    /** 视频名称 */
    private val mVideoName by intentExtrasNoNull(EXTRA_VIDEO_NAME, "")

    override fun startCreate() {
        super.startCreate()
        configWindow()
        getParameter(intent)
    }

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        mMediaView = mBinding.mediaView
        initMediaView()
    }

    override fun setListeners() {
        super.setListeners()
        mMediaView.setListener(object : MediaView.Listener {
            override fun onScreenChange(isFull: Boolean) {
                changeOrientation(isFull)
            }

            override fun onClickBack() {
                quit()
            }
        })

        // 数据流量播放提示页关闭
        mBinding.videoPhoneDataLayout.setBackListener{
            finish()
        }

        // 数据流量播放提示页 流量播放
        mBinding.videoPhoneDataLayout.setDataPlayListener{
            mBinding.videoPhoneDataLayout.hide()
            mMediaView.showLoading()
            mMediaView.reload()
        }

        mBinding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        super.initData()
        playVideo()
        initViewPager()
        mBinding.backBtn.bringToFront()
    }

    override fun onPressBack(): Boolean {
        quit()
        return true
    }

    /** 获取播放参数 */
    private fun getParameter(intent: Intent?) {
        if (intent == null) {
            toastShort(R.string.video_parameter_error)
            finish()
            return
        }
        Log.e(MediaView.TAG, "VideoName : $mVideoName")
        Log.e(MediaView.TAG, "VideoPath : $mVideoPath")
    }

    /** 初始化ViewPager */
    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        list.add(InfoFragment.newInstance())
        list.add(CommentFragment.newInstance())
        mBinding.vp.offscreenPageLimit = TAB_NAMES.size
        mBinding.vp.adapter = SimpleVp2Adapter(this, list)
        mBinding.tabLayout.setupViewPager(mBinding.vp, TAB_NAMES)
    }

    /** 设置全屏无状态栏 */
    private fun configWindow() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)  //取消标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)  //取消状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  //屏幕常亮
    }

    private fun initMediaView() {
        mMediaView.initMediaView(this)
        mMediaView.setTitle(mVideoName)
        mMediaView.setFullScreen(false)
        mMediaView.setVideoPath(mVideoPath)
        mBinding.videoPhoneDataLayout.needBackBtn(false)
    }

    /** 改变屏幕方向，是否横屏[isLandscape] */
    private fun changeOrientation(isLandscape: Boolean) {
        mMediaView.setFullScreen(isLandscape)
        val lp = mBinding.videoLayout.layoutParams
        lp.height = if (isLandscape) FrameLayout.LayoutParams.MATCH_PARENT else dp2px(200)
        mBinding.videoLayout.layoutParams = lp
        requestedOrientation = if (isLandscape) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mBinding.backBtn.visibility = if (isLandscape) View.GONE else View.VISIBLE
    }

    /** 播放视频 */
    private fun playVideo() {
        if (!NetworkManager.get().isWifi()) {// 流量下
            mBinding.videoPhoneDataLayout.show()
        } else {
            mBinding.videoPhoneDataLayout.hide()
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

    /** 退出 */
    private fun quit(){
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {// 全屏时转半屏
            changeOrientation(false)
        } else if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {//半屏退出
            finish()
        }
    }
}