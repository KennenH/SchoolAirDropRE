package com.example.schoolairdroprefactoredition.scene.main.extend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.adapter.HeaderFooterOnlyRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.SearchResultPagerAdapter
import com.example.schoolairdroprefactoredition.ui.components.SearchBar.OnSearchActionListener
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader
import com.example.schoolairdroprefactoredition.ui.components.SearchHistoryHeader.OnHistoryActionListener
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.component_search_bar.*
import kotlinx.android.synthetic.main.activity_search_prelayout.*
import kotlinx.android.synthetic.main.item_add_pic.view.*
import java.util.*

class SearchActivity : ImmersionStatusBarActivity(), OnSearchActionListener, SearchResultPagerAdapter.OnRecyclerActionListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
            if (context is AppCompatActivity) {
                context.overridePendingTransition(0, 0)
            }
        }
    }

    private val searchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val mSearchResultPagerAdapter by lazy {
        SearchResultPagerAdapter(supportFragmentManager)
    }

    /**
     * 历史搜索列表的适配器
     */
    private val mHistoryAdapter by lazy {
        HeaderFooterOnlyRecyclerAdapter()
    }

    private val mHistoryHeader by lazy {
        SearchHistoryHeader(this)
    }

    /**
     * 默认中国计量大学
     */
    private var longitude = AppConfig.DEBUG_LONGITUDE

    /**
     * 默认中国计量大学
     */
    private var latitude = AppConfig.DEBUG_LATITUDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_prelayout)
        setSupportActionBar(search_toolbar)

        init()

        longitude = intent?.getDoubleExtra(ConstantUtil.LONGITUDE, longitude) as Double
        latitude = intent?.getDoubleExtra(ConstantUtil.LATITUDE, latitude) as Double

        startAnimation()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(0, 0)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        mHistoryAdapter.removeHeaderView(mHistoryHeader)
        mHistoryAdapter.addHeaderView(mHistoryHeader)

        mSearchResultPagerAdapter.setOnRecyclerActionListener(this@SearchActivity)
        search_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // do nothing
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                // do nothing
            }
        })
        search_pager.adapter = mSearchResultPagerAdapter
        search_history.layoutManager = LinearLayoutManager(this@SearchActivity)
        search_history.adapter = mHistoryAdapter
        search.setOnSearchActionListener(this@SearchActivity)
        search_cancel.setOnClickListener {
            KeyboardUtils.hideSoftInput(this)
            finish()
            overridePendingTransition(0, 0)
        }
        search_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) search.closeSearch()
            }
        })
        refresh.visibility = View.INVISIBLE

        mHistoryHeader.showAfterUpdate(searchViewModel.getSearchHistories()?.historyList)
        mHistoryHeader.setOnHistoryActionListener(object : OnHistoryActionListener {
            override fun onDeleteHistory() {
                searchViewModel.deleteHistories()
                mHistoryHeader.showAfterUpdate(ArrayList())
            }

            override fun onSearchHistory(key: String) {
                search?.setInputKey(key)
                performSearch(key)
            }
        })
    }

    /**
     * 执行搜索
     * 显示结果列表
     *
     * @param key 搜索关键词
     */
    private fun performSearch(key: String) {
        if (key.trim() != "") {
            search?.closeSearch()

            showResultPager()
            // 通知当前显示的子搜索页面搜索关键词
            search_pager?.currentItem?.let { mSearchResultPagerAdapter.performSearch(it, key) }
            mHistoryHeader.showAfterUpdate(searchViewModel.getSearchHistories()?.historyList)
        }
    }

    /**
     * 开始搜索框移动动画，使得界面切换没有那么突兀
     */
    private fun startAnimation() {
        val constraintSet = ConstraintSet()
        // R.layout.activity_search 和 R.layout.activity_search_prelayout 除了动画部分其他view必须要一样
        constraintSet.clone(this, R.layout.activity_search)
        val transition: Transition = AutoTransition()
        transition.interpolator = DecelerateInterpolator()
        transition.duration = 200
        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                refresh?.visibility = View.VISIBLE
                search?.openSearch()
            }
        })

        root?.post {
            TransitionManager.beginDelayedTransition(root, transition)
            constraintSet.applyTo(root)
        }
    }

    /**
     * 隐藏子搜索fragment，显示外部搜索历史记录页
     *
     * 与[showResultPager]作用相反
     */
    private fun showHistory() {
        search_pager.visibility = View.GONE
        search_history.visibility = View.VISIBLE
    }

    /**
     * 显示搜索子fragment，隐藏外部搜索历史记录页
     *
     * 与[showHistory]作用相反
     */
    private fun showResultPager() {
        search_pager.visibility = View.VISIBLE
        search_history.visibility = View.GONE
    }

    override fun onSearch(key: String, v: View) {
        performSearch(key)
    }

    override fun onInputChanged(input: String) {
        // 搜索框中输入的文本不为空白则显示历史记录页
        if (input.trim() == "") {
            showHistory()
        }
    }

    override fun onFocusChanged(v: View, hasFocus: Boolean) {
        if (hasFocus) showHistory()
    }

    /**
     * 子fragment结果列表滑动事件，关闭搜索框
     */
    override fun onRecyclerScrolled() {
        search?.closeSearch()
    }
}