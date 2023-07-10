package com.lodz.android.mmsplayerdemo

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.mmsplayerdemo.databinding.ActivityMainBinding
import com.lodz.android.mmsplayerdemo.simple.SimpleVideoActivity
import com.lodz.android.mmsplayerdemo.widget.VideoActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

class MainActivity : BaseActivity() {

    private val mBinding: ActivityMainBinding by bindingLayout(ActivityMainBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.app_name)
        getTitleBarLayout().setBackgroundColor(getColorCompat(R.color.colorPrimary))
        getTitleBarLayout().needBackButton(false)
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.simpleBtn.setOnClickListener {
            mGetContentResult.launch("video/*")
        }

        mBinding.widgetBtn.setOnClickListener {
            VideoActivity.start(this, getString(R.string.info_name), "http://vd3.bdstatic.com/mda-nkdksvz89kgvez0j/360p/h264/1668436838593889961/mda-nkdksvz89kgvez0j.mp4")
        }
    }

    /** 单类型选择单文件回调 */
    private val mGetContentResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null){
            toastShort("取消选择")
            return@registerForActivityResult
        }
        SimpleVideoActivity.start(this, it)
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}
