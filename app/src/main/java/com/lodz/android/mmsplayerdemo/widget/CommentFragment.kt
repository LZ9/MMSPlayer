package com.lodz.android.mmsplayerdemo.widget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.mmsplayerdemo.databinding.FragmentCommentBinding
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup
import java.util.*
import kotlin.collections.ArrayList

/**
 * 评论
 * Created by zhouL on 2018/10/24.
 */
@SuppressLint("NotifyDataSetChanged")
class CommentFragment : LazyFragment() {
    companion object {
        fun newInstance(): CommentFragment = CommentFragment()
    }

    private val mBinding: FragmentCommentBinding by bindingLayout(FragmentCommentBinding::inflate)

    override fun getAbsViewBindingLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = mBinding.root

    private lateinit var mAdapter: CommentAdapter

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        mAdapter = mBinding.recyclerView
            .linear()
            .setup(CommentAdapter(requireContext()))
        mAdapter.setData(getData())
        mAdapter.notifyDataSetChanged()
    }

    private fun getData(): ArrayList<String> {
        val millis = System.currentTimeMillis()
        val list = ArrayList<String>()
        for (i in 0..50) {
            val date = DateUtils.getFormatString(DateUtils.TYPE_2, Date(millis - i * 50000000))
            list.add(date)
        }
        return list
    }

}