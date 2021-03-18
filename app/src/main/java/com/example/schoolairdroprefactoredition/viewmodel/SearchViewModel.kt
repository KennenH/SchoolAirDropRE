package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.cache.SearchHistories
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.domain.SearchSuggestionBean
import com.example.schoolairdroprefactoredition.repository.SearchRepository
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class SearchViewModel : ViewModel() {
    private var lastSearchedKey: String = ""

    private var lastLongitude = AppConfig.DEBUG_LONGITUDE

    private var lastLatitude = AppConfig.DEBUG_LATITUDE

    private var page = 1

    private val mSearchRepository by lazy {
        SearchRepository.getInstance()
    }

    private val mSearchSuggestions = MutableLiveData<SearchSuggestionBean>()

    /**
     * 删除历史搜索
     */
    fun deleteHistories() {
        mSearchRepository.deleteHistories()
    }

    /**
     * 在输入关键词之后上拉加载更多
     */
    fun getSearchResult(): LiveData<List<DomainPurchasing.DataBean>?> {
        val mSearchResults = MutableLiveData<List<DomainPurchasing.DataBean>?>()
        mSearchRepository.doSearchGoods(++page, lastLongitude, lastLatitude, lastSearchedKey, true) {
            mSearchResults.postValue(it?.data)
        }
        return mSearchResults
    }

    /**
     * 输入关键词后的第一次搜索
     */
    fun getSearchResult(longitude: Double, latitude: Double, key: String): LiveData<List<DomainPurchasing.DataBean>?> {
        val mSearchResults = MutableLiveData<List<DomainPurchasing.DataBean>?>()
        // 初始化页面号以及保存搜索关键字和地点
        page = 1
        lastSearchedKey = key
        lastLongitude = longitude
        lastLatitude = latitude

        mSearchRepository.doSearchGoods(page, longitude, latitude, key, false) {
            mSearchResults.postValue(it?.data)
        }
        return mSearchResults
    }

    fun getSearchHistories(): SearchHistories? {
        return mSearchRepository.getSearchHistory()
    }

    fun getSearchSuggestion(input: String): LiveData<SearchSuggestionBean> {
        mSearchRepository.getSearchSuggestion(input)
        return mSearchSuggestions
    }
}