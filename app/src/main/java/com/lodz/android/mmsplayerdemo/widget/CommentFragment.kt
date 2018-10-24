package com.lodz.android.mmsplayerdemo.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lodz.android.mmsplayerdemo.R

/**
 * 评论
 * Created by zhouL on 2018/10/24.
 */
class CommentFragment : Fragment() {
    companion object {
        fun newInstance(): CommentFragment = CommentFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_comment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }


}