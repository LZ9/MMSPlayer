package com.lodz.android.mmsplayerdemo.video.status

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.startAnim
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.mmsplayerdemo.R

/**
 * 播放器加载页面
 * Created by zhouL on 2018/10/23.
 */
class VideoLoadingLayout : LinearLayout {

    /** 返回按钮 */
    private val mBackBtn by bindView<ImageView>(R.id.back_btn)
    /** 数据提示列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)

    /** 提示列表适配器 */
    private lateinit var mAdapter: VideoLoadingAdapter
    /** 提示语列表 */
    private val mTipsList = ArrayList<String>()
    /** 监听器 */
    private var mListener: View.OnClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_video_loading, this)
        initRecyclerView()
        setListeners()
        bindData(context.getString(R.string.video_loading_player_init_complete))
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = VideoLoadingAdapter(context)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    private fun setListeners() {
        mBackBtn.setOnClickListener {
            if (mListener != null) {
                mListener!!.onClick(it)
            }
        }
    }

    /** 绑定数据[tips] */
    private fun bindData(tips: String) {
        mTipsList.add(tips)
        mAdapter.setData(mTipsList)
        mAdapter.notifyDataSetChanged()
        mRecyclerView.smoothScrollToPosition(mTipsList.size)
    }

    /** 设置返回按钮监听器[listener] */
    fun setBackListener(listener: View.OnClickListener) {
        mListener = listener
    }

    /** 加载页是否显示 */
    fun isShow(): Boolean = visibility == View.VISIBLE

    /** 显示加载页 */
    fun show() {
        if (!isShow()) {
            visibility = View.VISIBLE
        }
    }

    /** 隐藏加载页 */
    fun hide() {
        if (isShow()) {
            UiHandler.postDelayed({
                startAnim(context, R.anim.anim_fade_out, View.GONE)
            }, 300)
        }
    }

    /** 显示播放组件加载完成 */
    fun showPlayerComplete() {
        bindData(context.getString(R.string.video_loading_player_view_complete))
    }

    /** 显示加载视频地址完成 */
    fun showLoadUrlComplete() {
        bindData(context.getString(R.string.video_loading_load_url_complete))
    }

    /** 显示开始解析视频地址 */
    fun showStartAnalysisUrl() {
        bindData(context.getString(R.string.video_loading_start_analysis_url))
    }

    /** 显示解析视频地址完成 */
    fun showAnalysisUrlComplete() {
        bindData(context.getString(R.string.video_loading_analysis_url_complete))
    }

    /** 显示切换画质 */
    fun showStartChangeQuality() {
        bindData(context.getString(R.string.video_loading_start_change_quality))
    }

    /** 显示解析异常 */
    fun showAnalysisError() {
        bindData(context.getString(R.string.video_analysis_error_tips))
    }

    /** 显示换行 */
    fun showEnter() {
        bindData("")
    }

    /** 设置是否全屏[isFull] */
    fun setFullScreen(isFull: Boolean) {
        mBackBtn.visibility = if (isFull) View.VISIBLE else View.GONE
    }
}