package com.lodz.android.mmsplayerdemo.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.mmsplayerdemo.R
import java.util.*

/**
 * 评论
 * Created by zhouL on 2018/10/24.
 */
class CommentFragment : Fragment() {
    companion object {
        fun newInstance(): CommentFragment = CommentFragment()
    }


    private lateinit var mAdapter: CommentAdapter

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_comment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        initRecyclerView()
        mAdapter.setData(getData())
        mAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = CommentAdapter(requireContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    private fun getData(): List<String> {
        val millis = System.currentTimeMillis()
        val list = ArrayList<String>()
        for (i in 0..50) {
            val date = DateUtils.getFormatString(DateUtils.TYPE_2, Date(millis - i * 50000000))
            list.add(date)
        }
        return list
    }

}