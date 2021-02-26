package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.repository.PurchasingRepository
import kotlinx.coroutines.launch

class PurchasingViewModel : ViewModel() {

    private var nowPage = 0

    private var longitude = 120.36055

    private var latitude = 30.31747

    private val purchasingRepository by lazy {
        PurchasingRepository.getInstance()
    }

    /**
     * 第一次获取物品信息或者下拉刷新
     *
     * 成功则获取到淘物信息，否则将会返回null
     */
    fun getGoodsInfo(longitude: Double, latitude: Double): LiveData<DomainPurchasing?> {
        val purchasingLiveData = MutableLiveData<DomainPurchasing>()
        viewModelScope.launch {
            // 刷新时重置页码
            nowPage = 0
            // 刷新时重置加载更多时的地理位置
            this@PurchasingViewModel.longitude = longitude
            this@PurchasingViewModel.latitude = latitude
            purchasingRepository.getNearbyGoods(nowPage, longitude, latitude) { response ->
                purchasingLiveData.postValue(response)
            }
        }
        return purchasingLiveData
    }

    /**
     * 后续获取物品信息即上滑加载更多
     *
     * 成功则获取到淘物信息，否则将会返回null
     */
    fun getGoodsInfo(): LiveData<DomainPurchasing> {
        val purchasingLiveData = MutableLiveData<DomainPurchasing>()
        viewModelScope.launch {
            purchasingRepository.getNearbyGoods(++nowPage, longitude, latitude) { response ->
                purchasingLiveData.postValue(response)
            }
        }
        return purchasingLiveData
    }
}