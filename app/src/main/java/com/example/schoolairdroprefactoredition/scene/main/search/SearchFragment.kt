package com.example.schoolairdroprefactoredition.scene.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.cache.SearchHistories
import com.example.schoolairdroprefactoredition.databinding.FragmentSearchPrelayoutBinding
import com.example.schoolairdroprefactoredition.scene.base.BaseFragment
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.HomeGoodsRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.SearchBar.OnSearchActionListener
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader.OnHistoryActionListener
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.SearchViewModel
import java.util.*

class SearchFragment : BaseFragment(), OnSearchActionListener, EndlessRecyclerView.OnLoadMoreListener, BaseFooterAdapter.OnNoMoreDataListener {

    companion object {
        private var INSTANCE: SearchFragment? = null
        fun getInstance() = INSTANCE
                ?: SearchFragment().also {
                    INSTANCE = it
                }
    }

    private val searchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private var binding: FragmentSearchPrelayoutBinding? = null

    private val mHistoryAdapter by lazy {
        HeaderFooterOnlyRecyclerAdapter()
    }

//    private val mSuggestionAdapter by lazy {
//        SearchSuggestionRecyclerAdapter()
//    }

    private val mResultAdapter by lazy {
        HomeGoodsRecyclerAdapter()
    }

    private val mHistoryHeader by lazy {
        SearchHistoryHeader(context)
    }

    private var longitude = 0.0

    private var latitude = 0.0

    private var isHistoryShowing = false

    private var isSuggestionShowing = false

    private var isResultShowing = false

    private val constraintSet = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.share_element_post_pager)

        longitude = activity?.intent?.getDoubleExtra(ConstantUtil.LONGITUDE, longitude) as Double
        latitude = activity?.intent?.getDoubleExtra(ConstantUtil.LATITUDE, latitude) as Double
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchPrelayoutBinding.inflate(inflater, container, false)
        init()
        return binding?.getRoot()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (activity != null) {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        mResultAdapter.setOnNoMoreDataListener(this)

        mHistoryAdapter.removeHeaderView(mHistoryHeader)
        mHistoryAdapter.addHeaderView(mHistoryHeader)

        binding?.run {
            // set layout manager
            searchHistory.layoutManager = LinearLayoutManager(context)
//            searchSuggestion.layoutManager = LinearLayoutManager(context)
            searchResult.layoutManager = LinearLayoutManager(context)

            // set adapter
            searchHistory.adapter = mHistoryAdapter
//            searchSuggestion.adapter = mSuggestionAdapter
            searchResult.adapter = mResultAdapter

            search.setOnSearchActionListener(this@SearchFragment)
            searchCancel.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
            searchHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) binding?.search?.closeSearch()
                }
            })
//            searchSuggestion.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) binding?.search?.closeSearch()
//                }
//            })
            searchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) binding?.search?.closeSearch()
                }
            })
            searchResult.setOnLoadMoreListener(this@SearchFragment)
            refresh.visibility = View.INVISIBLE
        }

        searchViewModel.getSearchHistories().observeOnce(viewLifecycleOwner, {
            mHistoryHeader.showAfterUpdate(it.historyList)
        })

        mHistoryHeader.setOnHistoryActionListener(object : OnHistoryActionListener {
            override fun onDeleteHistory() {
                searchViewModel.deleteHistories()
                mHistoryHeader.showAfterUpdate(ArrayList())
            }

            override fun onSearchHistory(key: String) {
                binding?.search?.setInputKey(key)
                performSearch(key)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.root?.post {
            startAnimation()
        }
    }

    /**
     * 执行搜索
     * 显示结果列表
     *
     * @param key 搜索关键词
     */
    private fun performSearch(key: String) {
        if (key.trim() != "") {
            binding?.search?.closeSearch()
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING)
            searchViewModel.getSearchResult(longitude, latitude, key).observeOnce(viewLifecycleOwner, { data ->
                if (data.isNullOrEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_SEARCH)
                } else {
                    mResultAdapter.setList(data)
                    showResult()
                }
            })
            searchViewModel.getSearchHistories().observeOnce(viewLifecycleOwner, { histories: SearchHistories ->
                mHistoryHeader.showAfterUpdate(histories.historyList)
            })
        }
    }

    private fun startAnimation() {
        constraintSet.clone(context, R.layout.fragment_search)
        val transition: Transition = AutoTransition()
        transition.interpolator = DecelerateInterpolator()
        transition.duration = 150
        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                binding?.refresh?.visibility = View.VISIBLE
                binding?.search?.openSearch()
            }
        })
        binding?.let {
            TransitionManager.beginDelayedTransition(it.root, transition)
            constraintSet.applyTo(it.root)
        }
    }

    private fun showHistory() {
        isHistoryShowing = true
        isSuggestionShowing = false
        isResultShowing = false
        binding?.apply {
//            searchSuggestion.visibility = View.GONE
            searchResult.visibility = View.GONE
            searchHistory.visibility = View.VISIBLE
            searchPlaceHolder.visibility = View.GONE
        }
    }

    private fun showSuggestion() {
        isHistoryShowing = false
        isSuggestionShowing = true
        isResultShowing = false
        binding?.apply {
            searchHistory.visibility = View.GONE
            searchResult.visibility = View.GONE
//            searchSuggestion.visibility = View.VISIBLE
            searchPlaceHolder.visibility = View.GONE
        }
    }

    private fun showResult() {
        isHistoryShowing = false
        isSuggestionShowing = false
        isResultShowing = true
        binding?.apply {
            searchHistory.visibility = View.GONE
//            searchSuggestion.visibility = View.GONE
            searchResult.visibility = View.VISIBLE
            searchPlaceHolder.visibility = View.GONE
        }
    }

    private fun showPlaceHolder(type: Int) {
        isHistoryShowing = false
        isSuggestionShowing = false
        isResultShowing = false
        binding?.apply {
            searchHistory.visibility = View.GONE
//            searchSuggestion.visibility = View.GONE
            searchResult.visibility = View.GONE
            searchPlaceHolder.visibility = View.VISIBLE
            searchPlaceHolder.setPlaceholderType(type)
        }
    }

    /**
     * 上拉自动加载更多
     *
     * 自动加载同一个关键词搜索的下一页
     */
    override fun autoLoadMore(recycler: EndlessRecyclerView) {
        searchViewModel.getSearchResult().observeOnce(viewLifecycleOwner) {
            recycler.finishLoading()
            if (it != null) {
                mResultAdapter.addData(it)
            }
        }
    }

    override fun onSearch(key: String, v: View) {
        performSearch(key)
    }

    override fun onInputChanged(input: String) {
        if (input.trim() == "") {
            showHistory()
        }
//        else showSuggestion()
    }

    override fun onFocusChanged(v: View, hasFocus: Boolean) {
        if (hasFocus) showHistory()
    }

    override fun onNoMoreData() {
        binding?.searchResult?.setIsNoMoreData(true)
    }

    override fun onNoMoreDataRefresh() {
        binding?.searchResult?.setIsNoMoreData(false)
    }

}