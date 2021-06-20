package com.example.schoolairdroprefactoredition.scene.main.extend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSearchChildBinding
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.SearchViewModel


/**
 * @author kennen
 * @date 2021/5/3
 */
class SearchIWantFragment private constructor() :
        StatePlaceholderFragment(),
        IWantRecyclerAdapter.OnNoMoreDataListener,
        EndlessRecyclerView.OnLoadMoreListener {

    companion object {
        private var INSTANCE: SearchIWantFragment? = null
        fun getInstance() = INSTANCE
                ?: SearchIWantFragment().also {
                    INSTANCE = it
                }
    }

    private var onSearchRecyclerActionListener: OnSearchChildFragmentActionListener? = null

    /**
     * 默认中国计量大学
     */
    private var longitude = AppConfig.DEBUG_LONGITUDE

    /**
     * 默认中国计量大学
     */
    private var latitude = AppConfig.DEBUG_LATITUDE

    private val mResultAdapter by lazy {
        IWantRecyclerAdapter()
    }

    private val searchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private lateinit var binding: FragmentSearchChildBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchChildBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        longitude = activity?.intent?.getDoubleExtra(ConstantUtil.LONGITUDE, longitude) as Double
        latitude = activity?.intent?.getDoubleExtra(ConstantUtil.LATITUDE, latitude) as Double

        mResultAdapter.addOnNoMoreDataListener(this)

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
            searchResult.setOnLoadMoreListener(this@SearchIWantFragment)
        }
    }

    fun setOnSearchChildFragmentRecyclerActionListener(listener: OnSearchChildFragmentActionListener) {
        onSearchRecyclerActionListener = listener
    }

    override fun getStatePlaceholder(): StatePlaceHolder {
        return binding.searchPlaceHolder
    }

    override fun getContentContainer(): View {
        return binding.searchResult
    }

    /**
     * 执行求购搜索逻辑
     */
    fun performSearchIWant(key: String) {
        binding.searchPlaceHolder.post {
            showPlaceholder(StatePlaceHolder.TYPE_LOADING)
            searchViewModel.searchIWantResult(
                    if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                    if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
                    key).observeOnce(viewLifecycleOwner, { data ->
                when {
                    data == null -> {
                        showPlaceholder(StatePlaceHolder.TYPE_ERROR,context?.getString(R.string.retryLater))
                    }
                    data.isEmpty() -> {
                        showPlaceholder(StatePlaceHolder.TYPE_EMPTY_SEARCH, context?.getString(R.string.simplifyYourKey))
                    }
                    else -> {
                        mResultAdapter.setList(data)
                        showContentContainer()
                    }
                }
            })
        }
    }

    override fun onNoMoreData() {
        binding.searchResult.setIsNoMoreData(true)
    }

    override fun onNoMoreDataRefresh() {
        binding.searchResult.setIsNoMoreData(false)
    }

    override fun autoLoadMore(recycler: EndlessRecyclerView) {
        searchViewModel.searchIWantResult().observeOnce(viewLifecycleOwner) {
            recycler.finishLoading()
            if (it != null) {
                mResultAdapter.addData(it)
            } else {
                Toast.makeText(context,R.string.errorLoading,Toast.LENGTH_SHORT).show()
            }
        }
    }


}