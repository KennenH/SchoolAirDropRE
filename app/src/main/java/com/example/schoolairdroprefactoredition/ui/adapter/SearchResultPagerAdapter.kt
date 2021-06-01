package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.scene.main.extend.OnSearchChildFragmentActionListener
import com.example.schoolairdroprefactoredition.scene.main.extend.SearchGoodsFragment
import com.example.schoolairdroprefactoredition.scene.main.extend.SearchIWantFragment


/**
 * @author kennen
 * @date 2021/5/3
 *
 * 管理物品、求购子搜索页面的adapter
 */
class SearchResultPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT),
        OnSearchChildFragmentActionListener {

    private var onRecyclerActionListeners: OnRecyclerActionListener? = null

    private var searchIWantFragment: SearchIWantFragment? = null

    private var searchGoodsFragment: SearchGoodsFragment? = null

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 1) {
            SearchIWantFragment.getInstance().also {
                it.setOnSearchChildFragmentRecyclerActionListener(this)
                searchIWantFragment = it
            }
        } else {
            SearchGoodsFragment.getInstance().also {
                it.setOnSearchChildFragmentRecyclerActionListener(this)
                searchGoodsFragment = it
            }
        }
    }

    /**
     * 根据当前显示的搜索页面执行对应的搜索操作
     */
    fun performSearch(fragmentPos: Int, key: String) {
        if (fragmentPos == 1) {
            searchIWantFragment?.performSearchIWant(key)
        } else {
            searchGoodsFragment?.performSearchGoods(key)
        }
    }

    interface OnRecyclerActionListener {

        /**
         * 搜索结果列表被上下滑动
         */
        fun onRecyclerScrolled()
    }

    fun setOnRecyclerActionListener(listener: OnRecyclerActionListener) {
        onRecyclerActionListeners = listener
    }

    /**
     * 所有子搜索fragment在本adapter中保持引用并注册列表滑动监听，当任一搜索子fragment列表滑动时将会通知到
     * 本adapter中，然后再通过[OnRecyclerActionListener]通知到
     * 主搜索页面[com.example.schoolairdroprefactoredition.scene.main.extend.SearchActivity]中
     */
    override fun onSearchChildRecyclerScrolled() {
        // 所有子fragment的滑动通知在这里进行中转并被通知到主搜索页面
        onRecyclerActionListeners?.onRecyclerScrolled()
    }
}