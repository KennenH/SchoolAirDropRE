package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.SearchHistories
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.repository.SearchRepository
import com.example.schoolairdroprefactoredition.utils.AppConfig

class SearchViewModel : ViewModel() {

    /**
     * 第一次搜索时保存的搜索关键词，以便用户下拉时继续获取下一页内容
     *
     * 物品搜索关键字
     */
    private var lastGoodsSearchedKey: String = ""

    /**
     * 同上
     *
     * 求购搜索关键字
     */
    private var lastIWantSearchKey: String = ""

    private var lastLongitude = AppConfig.DEBUG_LONGITUDE

    private var lastLatitude = AppConfig.DEBUG_LATITUDE

    /**
     * 搜索物品当前查看的页码
     */
    private var goodsPage = 1

    /**
     * 搜索求购当前查看的页码
     */
    private var iwantPage = 1

    private val mSearchRepository by lazy {
        SearchRepository.getInstance()
    }

    /**
     * 删除历史搜索
     *
     * 包括搜索物品和求购的历史关键字都会被删除
     */
    fun deleteHistories() {
        mSearchRepository.deleteHistories()
    }

    /**
     * 获取历史搜索记录
     *
     * 包括搜索物品和求购的关键字都会被查询出来
     */
    fun getSearchHistories(): SearchHistories? {
        return mSearchRepository.getSearchHistory()
    }

    /**
     * 搜索物品上拉加载更多
     */
    fun searchGoodsResult(): LiveData<List<DomainPurchasing.DataBean>?> {
        val result = MutableLiveData<List<DomainPurchasing.DataBean>?>()
        mSearchRepository.doSearchGoods(lastGoodsSearchedKey, ++goodsPage, lastLongitude, lastLatitude, true) {
            result.postValue(it?.data)
        }
        return result
    }

    /**
     * 输入关键词后对物品的第一次搜索
     */
    fun searchGoodsResult(longitude: Double, latitude: Double, key: String): LiveData<List<DomainPurchasing.DataBean>?> {
        val result = MutableLiveData<List<DomainPurchasing.DataBean>?>()
        // 初始化页面号以及保存搜索关键字和地点
        goodsPage = 1
        lastGoodsSearchedKey = key
        lastLongitude = longitude
        lastLatitude = latitude

        mSearchRepository.doSearchGoods(key, goodsPage, longitude, latitude, false) {
            result.postValue(it?.data)
        }
        return result
    }

    /**
     * 输入关键词后对求购的第一次搜索
     */
    fun searchIWantResult(longitude: Double, latitude: Double, key: String): LiveData<List<DomainIWant.Data>?> {
        val result = MutableLiveData<List<DomainIWant.Data>?>()
        // 初始化页面号并保存搜索关键字和地点
        iwantPage = 1
        lastIWantSearchKey = key
        lastLongitude = longitude
        lastLatitude = latitude

        mSearchRepository.doSearchIWant(key, iwantPage, longitude, latitude, false) {
            LogUtils.d(it)
            result.postValue(it?.data)
        }
        return result
    }

    /**
     * 搜索求购上拉加载更多
     */
    fun searchIWantResult(): LiveData<List<DomainIWant.Data>?> {
        val result = MutableLiveData<List<DomainIWant.Data>?>()
        mSearchRepository.doSearchIWant(lastIWantSearchKey, ++iwantPage, lastLongitude, lastLatitude, true) {
            LogUtils.d(it)
            result.postValue(it?.data)
        }
        return result
    }
}