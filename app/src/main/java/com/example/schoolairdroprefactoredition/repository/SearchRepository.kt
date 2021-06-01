package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.SearchHistories
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.utils.AppConfig
import java.util.*

class SearchRepository {

    companion object {

        /**
         * 搜索关键词最大保存数量
         */
        private const val HISTORY_MAX_SAVED = 15

        private var INSTANCE: SearchRepository? = null
        fun getInstance() = INSTANCE
                ?: SearchRepository().also {
                    INSTANCE = it
                }
    }

    private val mJsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 保存搜索历史记录
     */
    private fun saveHistory(history: String) {
        var histories = mJsonCacheUtil.getCache(SearchHistories.KEY, SearchHistories::class.java)
        // 如果之前已经有了这个关键字，去掉之前的，也是为了省去排序的麻烦，保证最新的搜索关键字一定处于第一个位置
        var list: MutableList<String>? = null
        if (histories != null && histories.historyList != null) {
            list = histories.historyList
            if (list.contains(history)) {
                list.remove(history)
            }
        }

        if (list == null) list = ArrayList()

        if (histories == null) histories = SearchHistories()

        histories.historyList = list
        if (list.size > HISTORY_MAX_SAVED) {
            list = list.subList(0, HISTORY_MAX_SAVED)
        }

        list.add(0, history)
        mJsonCacheUtil.saveCache(SearchHistories.KEY, histories)
    }

    /**
     * 按关键词搜索物品
     *
     * @param isLoadMore 是否是上拉自动加载更多产生的请求
     */
    fun doSearchGoods(
            key: String,
            page: Int,
            longitude: Double,
            latitude: Double,
            isLoadMore: Boolean,
            onResult: (DomainPurchasing?) -> Unit) {
        // 若不是上拉加载更多，说明该关键字为新关键字，需要保存
        if (!isLoadMore) {
            saveHistory(key)
        }

        RetrofitClient.goodsApi.searchGoods(
                ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET,
                key, page,
                longitude, latitude,
        ).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 按关键字搜索附近求购
     */
    fun doSearchIWant(
            key: String,
            page: Int,
            longitude: Double,
            latitude: Double,
            isLoadMore: Boolean,
            onResult: (DomainIWant?) -> Unit) {
        // 若不是上拉加载更多，说明该关键字为新关键字，需要保存
        if (!isLoadMore) {
            saveHistory(key)
        }

        RetrofitClient.iWantApi.searchIWant(
                ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET,
                key, page,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
        ).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 获取搜索历史
     */
    fun getSearchHistory(): SearchHistories? {
        val histories = mJsonCacheUtil.getCache(SearchHistories.KEY, SearchHistories::class.java)
        return if (histories != null && histories.historyList != null && histories.historyList.size > 0) {
            histories
        } else {
            null
        }
    }

    /**
     * 获取搜索建议
     */
    fun getSearchSuggestion(key: String) {
        val histories = mJsonCacheUtil.getCache(SearchHistories.KEY, SearchHistories::class.java)?.historyList

    }

    /**
     * 删除历史搜索
     */
    fun deleteHistories() {
        mJsonCacheUtil.deleteCache(SearchHistories.KEY)
    }
}