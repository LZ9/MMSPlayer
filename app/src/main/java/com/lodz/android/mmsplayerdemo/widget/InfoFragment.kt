package com.lodz.android.mmsplayerdemo.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.mmsplayerdemo.databinding.FragmentInfoBinding
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 简介
 * Created by zhouL on 2018/10/24.
 */
class InfoFragment : LazyFragment() {
    companion object {
        fun newInstance(): InfoFragment = InfoFragment()
    }

    private val mBinding: FragmentInfoBinding by bindingLayout(FragmentInfoBinding::inflate)

    override fun getAbsViewBindingLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = mBinding.root
}