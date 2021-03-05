package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.GoodsRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class GoodsViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class GoodsViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GoodsViewModel::class.java)) {
                return GoodsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private val goodsRepository by lazy {
        GoodsRepository.getInstance()
    }


    /**
     * 获取物品详细信息
     *
     * @return
     * null 物品信息获取失败
     * code 200 获取成功
     * code 404 物品已下架
     */
    @Deprecated("使用getGoodsAllDetailByID")
    fun getGoodsDetailByID(goodsID: Int): LiveData<GoodsDetailInfo?> {
        val goodsDetail = MutableLiveData<GoodsDetailInfo?>()
        viewModelScope.launch {
            goodsRepository.getGoodsDetail(goodsID) { response ->
                response.let {
                    if (it != null) {
                        viewModelScope.launch {
                            it.isFavorite = databaseRepository.isFavorite(goodsID)
                            goodsDetail.postValue(it)
                        }
                    } else {
                        goodsDetail.postValue(null)
                    }
                }
            }
        }
        return goodsDetail
    }

    /**
     * 获取物品全部详细信息
     * @return
     * null 物品信息获取失败
     * 非空 code == 200 信息获取成功
     * 非空 code == 404 物品已被下架
     */
    fun getGoodsAllDetailByID(goodsID: Int): LiveData<DomainGoodsAllDetailInfo?> {
        val goodsAllDetail = MutableLiveData<DomainGoodsAllDetailInfo?>()
        viewModelScope.launch {
            goodsRepository.getGoodsAllDetail(goodsID) {
                if (it != null) {
                    // 物品信息获取成功，则保存用户缓存
                    if (it.code == ConstantUtil.HTTP_OK) {
                        it.data.seller.apply {
                            viewModelScope.launch {
                                databaseRepository.saveUserCache(UserCache(
                                        user_id,
                                        user_name,
                                        user_avatar,
                                        createtime,
                                        user_goodsOnSaleCount,
                                        user_contactCount))
                            }
                        }
                    }

                    viewModelScope.launch {
                        it.data.goods_is_favored = databaseRepository.isFavorite(goodsID)
                        goodsAllDetail.postValue(it)
                    }
                } else {
                    goodsAllDetail.postValue(null)
                }
            }
        }
        return goodsAllDetail
    }

    /**
     * 切换物品收藏状态
     *
     * @return
     * true 收藏成功
     * false 取消收藏成功
     */
    fun toggleGoodsFavorite(favorite: Favorite): LiveData<Boolean> {
        val favoriteGoodsLiveData: MutableLiveData<Boolean> = MutableLiveData()
        viewModelScope.launch {
            databaseRepository.isFavorite(favorite.goods_id).let {
                if (it) {
                    databaseRepository.removeFavorite(favorite.goods_id)
                    favoriteGoodsLiveData.postValue(false)
                } else {
                    databaseRepository.addFavorite(favorite)
                    favoriteGoodsLiveData.postValue(true)
                }
            }
        }
        return favoriteGoodsLiveData
    }


}