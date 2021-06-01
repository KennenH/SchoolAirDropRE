package com.example.schoolairdroprefactoredition.scene.ssb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.adapter.SellingAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.SellingAdapter.OnSSBItemActionListener
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.SSBViewModel

abstract class SSBBaseFragment :
        StatePlaceholderFragment(){

    protected val viewModel by lazy {
        ViewModelProvider(this).get(SSBViewModel::class.java)
    }

    protected var binding: FragmentSsbBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSsbBinding.inflate(inflater, container, false)
        binding?.apply {
            ssbRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            ssbRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (activity is SSBActivity) {
                        (activity as? SSBActivity)?.hideSearchBar()
                    }
                }
            })
        }

        init(binding)
        return binding?.getRoot()
    }

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return binding?.placeHolder
    }

    override fun getContentContainer(): View? {
        return binding?.ssbRecycler
    }

    /**
     * 初始化页面
     */
    abstract fun init(binding: FragmentSsbBinding?)

    /**
     * 按下刷新重试网络数据获取
     */
    abstract fun retryGrabOnlineData()

    /**
     * item上右下角的三个点按钮的操作
     * 在售列表中为 下架物品、修改物品信息
     */
    abstract fun onItemAction(view: View, bean: Any?)
}