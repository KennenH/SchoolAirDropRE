package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.database.pojo.PurchasingCache
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.PurchasingRepository
import com.example.schoolairdroprefactoredition.utils.AppConfig
import kotlinx.coroutines.launch

class PurchasingViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class PurchasingViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PurchasingViewModel::class.java)) {
                return PurchasingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private var nowPage = 0

    private var longitude = AppConfig.DEBUG_LONGITUDE
    private var latitude = AppConfig.DEBUG_LATITUDE

    private val purchasingRepository by lazy {
        PurchasingRepository.getInstance()
    }

    /**
     * 获取上次app打开或者刷新时缓存的物品信息，仅在淘物页面被加载时才调用一次
     */
    fun getPurchasingCache(): LiveData<DomainPurchasing?> {
        val purchasingCache = MutableLiveData<DomainPurchasing?>()
        viewModelScope.launch {
            purchasingCache.postValue(DomainPurchasing().also { wrapper ->
                val cacheList = databaseRepository.getPurchasingCache()
                wrapper.data = ArrayList<DomainPurchasing.DataBean>().also { wrapperData ->
                    for (cache in cacheList) {
                        wrapperData.add(DomainPurchasing.DataBean().also { data ->
                            data.seller = DomainPurchasing.DataBean.SellerBean().also { dataSeller ->
                                dataSeller.user_name = cache.user_name
                                dataSeller.user_avatar = cache.user_avatar
                            }
                            data.goods_id = cache.goods_id
                            data.goods_name = cache.goods_name
                            data.goods_cover_image = cache.goods_cover_image
                            data.goods_price = cache.goods_price
                            data.isGoods_is_bargain = cache.goods_is_bargain
                            data.isGoods_is_secondHand = cache.goods_is_secondhand
                        })
                    }
                }
            })
        }
        return purchasingCache
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
            // 刷新时重置加载更多的地理位置
            this@PurchasingViewModel.longitude = longitude
            this@PurchasingViewModel.latitude = latitude
            purchasingRepository.getNearbyGoods(nowPage, longitude, latitude) {
                // 若获取成功则进行缓存，下次打开app将优先从缓存中获取
                if (it != null) {
                    viewModelScope.launch {
                        ArrayList<PurchasingCache>(it.data.size).apply {
                            for (datum in it.data) {
                                add(PurchasingCache(
                                        datum.goods_id,
                                        datum.goods_name,
                                        datum.goods_cover_image,
                                        datum.isGoods_is_bargain,
                                        datum.isGoods_is_secondHand,
                                        datum.goods_price,
                                        datum.seller.user_name,
                                        datum.seller.user_avatar))
                            }
                            databaseRepository.savePurchasingCache(this)
                        }
                    }
                }
                purchasingLiveData.postValue(it)
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