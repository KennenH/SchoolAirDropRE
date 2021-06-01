package com.example.schoolairdroprefactoredition.scene.main.extend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSearchChildBinding
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.PurchasingRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.SearchResultPagerAdapter
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.SearchViewModel


/**
 * @author kennen
 * @date 2021/5/3
 *
 * 搜索物品页面，作为搜索页面的子页面
 */
class SearchGoodsFragment private constructor() :
        StatePlaceholderFragment(),
        EndlessRecyclerView.OnLoadMoreListener,
        BaseFooterAdapter.OnNoMoreDataListener {

    companion object {
        private var INSTANCE: SearchGoodsFragment? = null
        fun getInstance() = INSTANCE
                ?: SearchGoodsFragment().also {
                    INSTANCE = it
                }
    }

    private var onSearchRecyclerActionListener: OnSearchChildFragmentActionListener? = null

    private val searchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    /**
     * 结果列表适配器
     */
    private val mResultAdapter by lazy {
        PurchasingRecyclerAdapter()
    }

    /**
     * 默认中国计量大学
     */
    private var longitude = AppConfig.DEBUG_LONGITUDE

    /**
     * 默认中国计量大学
     */
    private var latitude = AppConfig.DEBUG_LATITUDE

    private lateinit var binding: FragmentSearchChildBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchChildBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        longitude = activity?.intent?.getDoubleExtra(ConstantUtil.LONGITUDE, longitude) as Double
        latitude = activity?.intent?.getDoubleExtra(ConstantUtil.LATITUDE, latitude) as Double

        mResultAdapter.setOnNoMoreDataListener(this)

        binding.run {
            // set layout manager
            searchResult.layoutManager = LinearLayoutManager(context)
            // set adapter
            searchResult.adapter = mResultAdapter
            searchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        onSearchRecyclerActionListener?.onSearchChildRecyclerScrolled()
                    }
                }
            })
            searchResult.setOnLoadMoreListener(this@SearchGoodsFragment)
        }
    }

    fun setOnSearchChildFragmentRecyclerActionListener(listener: OnSearchChildFragmentActionListener) {
        onSearchRecyclerActionListener = listener
    }

    /**
     * 执行搜索物品逻辑
     */
    fun performSearchGoods(key: String) {
        binding.searchPlaceHolder.post {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING)
            searchViewModel.searchGoodsResult(
                    if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                    if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
                    key).observeOnce(viewLifecycleOwner, { data ->
                if (data.isNullOrEmpty()) {
                    showPlaceholder(StatePlaceHolder.TYPE_EMPTY_SEARCH, context?.getString(R.string.simplifyYourKey))
                } else {
                    mResultAdapter.setList(data)
                    showContentContainer()
                }
            })
        }
    }

    override fun getStatePlaceholder(): StatePlaceHolder {
        return binding.searchPlaceHolder
    }

    override fun getContentContainer(): View {
        return binding.searchResult
    }

    override fun autoLoadMore(recycler: EndlessRecyclerView) {
        searchViewModel.searchGoodsResult().observeOnce(viewLifecycleOwner) {
            recycler.finishLoading()
            if (it != null) {
                mResultAdapter.addData(it)
            }
        }
    }

    override fun onNoMoreData() {
        binding.searchResult.setIsNoMoreData(true)
    }

    override fun resetNoMoreData() {
        binding.searchResult.setIsNoMoreData(false)
    }
}