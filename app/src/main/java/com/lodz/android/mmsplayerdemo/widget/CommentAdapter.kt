package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.mmsplayerdemo.R
import java.util.*

/**
 * 评论列表
 * Created by zhouL on 2018/10/29.
 */
class CommentAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NAME_ITEM = arrayListOf("张", "陈", "王", "李", "赵", "周", "黄", "苏")
    private val COMMENT_ITEM = "是读千万家让逻辑单例创建旭晶无饿哦斯迪克沃尔巨坑参加过问排起物业卡卡是语文切勿偶尔软件新春快乐实际打卡罗杰斯欧文吗去我跑提出张先生快圣诞节饭"

    private var mData: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = CommentViewHolder(getLayoutView(parent, R.layout.rv_item_comment))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentViewHolder) {
            showItem(holder, position)
        }
    }

    private fun showItem(holder: CommentViewHolder, position: Int) {
        setName(holder.nameTv)
        holder.dateTv.text = getItem(position)
        setComment(holder.commentTv)
    }

    private fun setName(textView: TextView) {
        val name = NAME_ITEM[Random().nextInt(NAME_ITEM.size)] + NAME_ITEM[Random().nextInt(NAME_ITEM.size)] + NAME_ITEM[Random().nextInt(NAME_ITEM.size)]
        textView.text = name
    }

    private fun setComment(textView: TextView) {
        val startIndex = Random().nextInt(COMMENT_ITEM.length / 2)
        val endIndex = Random().nextInt(COMMENT_ITEM.length / 2) + COMMENT_ITEM.length / 2
        textView.text = COMMENT_ITEM.substring(startIndex, endIndex)
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 姓名 */
        val nameTv by lazy {
            itemView.findViewById<TextView>(R.id.name_tv)
        }
        /** 日期 */
        val dateTv by lazy {
            itemView.findViewById<TextView>(R.id.date_tv)
        }
        /** 评论内容 */
        val commentTv by lazy {
            itemView.findViewById<TextView>(R.id.comment_tv)
        }
    }

    fun setData(list: List<String>) {
        mData = list
    }

    fun getData(): List<String>? = mData

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