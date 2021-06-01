package com.example.schoolairdroprefactoredition.scene.main.extend

/**
 * @author kennen
 * @date 2021/5/19
 *
 * 监听子fragment列表滑动动作通知到
 * [com.example.schoolairdroprefactoredition.ui.adapter.SearchResultPagerAdapter]中
 */
interface OnSearchChildFragmentActionListener {

    /**
     * 搜索子fragment结果列表被滑动
     */
    fun onSearchChildRecyclerScrolled()
}