package com.lodz.android.mmsplayerdemo.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.lodz.android.mmsplayerdemo.databinding.RvItemCommentBinding
import com.lodz.android.pandora.widget.rv.recycler.base.BaseVbRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder
import java.util.*

/**
 * 评论列表
 * Created by zhouL on 2018/10/29.
 */
class CommentAdapter(context: Context) : BaseVbRvAdapter<String>(context) {

    companion object{
        private val NAME_ITEM = arrayListOf("张", "陈", "王", "李", "赵", "周", "黄", "苏")
        private val COMMENT_ITEM = "是读千万家让逻辑单例创建旭晶无饿哦斯迪克沃尔巨坑参加过问排起物业卡卡是语文切勿偶尔软件新春快乐实际打卡罗杰斯欧文吗去我跑提出张先生快圣诞节饭"
    }

    override fun getVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding = RvItemCommentBinding::inflate

    override fun onBind(holder: DataVBViewHolder, position: Int) {
        holder.getVB<RvItemCommentBinding>().apply {
            setName(nameTv)
            dateTv.text = getItem(position) ?: ""
            setComment(commentTv)
        }
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
}