package com.example.schoolairdroprefactoredition.scene.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

abstract class TabBaseFragment : StatePlaceholderFragment() {

    private var binding: FragmentRecyclerBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)

        val index = arguments?.getInt(ConstantUtil.KEY_ARG_SECTION_NUMBER, 0)

        init(binding, index)
        getTabData(binding, index)

        return binding?.root
    }

    /**
     * 初始化
     */
    protected open fun init(binding: FragmentRecyclerBinding?, index: Int?) {
    }

    /**
     * 获取Tab上的数据
     */
    open protected fun getTabData(binding: FragmentRecyclerBinding?, index: Int?) {
        var token: DomainAuthorize? = null
        try {
            token = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE) as DomainAuthorize
        } catch (ignored: NullPointerException) {
        }

        if (token == null) {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
            return
        }

        showPlaceholder(StatePlaceHolder.TYPE_LOADING)

        if (index == 0) {
            loadFirstTabData(binding, token)
        } else if (index == 1) {
            loadSecondTabData(binding, token)
        }
    }

    /**
     * 加载第一个tab页面内容
     */
    abstract fun loadFirstTabData(binding: FragmentRecyclerBinding?, token: DomainAuthorize)

    /**
     * 加载第二个tab页面内容
     */
    abstract fun loadSecondTabData(binding: FragmentRecyclerBinding?, token: DomainAuthorize)

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return binding?.placeholder
    }

    override fun getContentContainer(): View? {
        return binding?.recycler
    }
}