package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallbackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.SearchHistories
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class SearchRepository {

    companion object {
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
     * 搜索关键字最大保存数
     */
    private val historyMaxStack = 15

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
        if (list.size > historyMaxStack) {
            list = list.subList(0, historyMaxStack)
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
            page: Int,
            longitude: Double,
            latitude: Double,
            key: String,
            isLoadMore: Boolean,
            onResult: (DomainPurchasing?) -> Unit) {
        // 若不是上拉加载更多，说明该关键字为新关键字，需要保存
        if (!isLoadMore) {
            LogUtils.d("保存搜索记录")
            saveHistory(key)
        }

        RetrofitClient.goodsApi.searchGoods(
                ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, page,
                longitude, latitude,
                key).apply {
            enqueue(object : CallbackWithRetry<DomainPurchasing>(this@apply) {
                override fun onResponse(call: Call<DomainPurchasing>, response: Response<DomainPurchasing>) {
                    val code = response.code()
                    if (code == HttpURLConnection.HTTP_OK) {
                        val info = response.body()
                        onResult(info)
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailureAllRetries() {
                    onResult(null)
                }
            })
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