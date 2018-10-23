package com.lodz.android.mmsplayerdemo.video.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.mmsplayerdemo.R

/**
 * 视频加载界面的文字提示适配器
 * Created by zhouL on 2018/10/23.
 */
class VideoLoadingAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = TipsViewHolder(getLayoutView(parent, R.layout.view_video_loading_tips))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TipsViewHolder) {
            showItem(holder, position)
        }
    }

    private fun showItem(holder: TipsViewHolder, position: Int) {
        val tips = getItem(position)
        if (tips.isNullOrEmpty()){
            return
        }

        holder.tipsTv.text = tips
    }

    fun setData(list: List<String>) {
        mData = list
    }

    fun getData(): List<String>? = mData

    inner class TipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 提示语 */
        val tipsTv by lazy {
            itemView.findViewById<TextView>(R.id.tips_tv)
        }
    }

    override fun getItemCount(): Int = if (mData != null) mData!!.size else 0

    /** 根据列表索引[position]获取数据 */
    private fun getItem(position: Int): String? {
        if (mData == null || mData!!.size == 0) {
            return null
        }
        try {
            return mData!!.get(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 在onCreateViewHolder方法中根据layoutId获取View */
    private fun getLayoutView(parent: ViewGroup, layoutId: Int): View =
            LayoutInflater.from(context).inflate(layoutId, parent, false)

}