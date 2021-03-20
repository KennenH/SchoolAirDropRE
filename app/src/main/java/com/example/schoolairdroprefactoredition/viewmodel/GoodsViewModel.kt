package com.example.schoolairdroprefactoredition.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.GoodsRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.cache.JsonCacheConstantUtil
import com.example.schoolairdroprefactoredition.cache.JsonCacheUtil
import kotlinx.coroutines.launch

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

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
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
     * null 收藏动作被拦截，操作过于频繁
     */
    fun toggleGoodsFavorite(context: Context, token: String, favorite: Favorite): LiveData<Boolean?> {
        val favoriteGoodsLiveData: MutableLiveData<Boolean?> = MutableLiveData()
        // 检查操作是否触发CoolDown
        JsonCacheUtil.runWithFrequentCheck(context,{
            viewModelScope.launch {
                databaseRepository.isFavorite(favorite.goods_id).let { isFavor ->
                    goodsRepository.favorGoods(token, favorite.goods_id, isFavor) {
                        viewModelScope.launch {
                            if (isFavor) {
                                databaseRepository.removeFavorite(favorite.goods_id)
                                favoriteGoodsLiveData.postValue(false)
                            } else {
                                databaseRepository.addFavorite(favorite)
                                favoriteGoodsLiveData.postValue(true)
                            }
                        }
                    }
                }
            }
        }, {
            // 操作过于频繁
            favoriteGoodsLiveData.postValue(null)
        })
        return favoriteGoodsLiveData
    }

    /**
     * 浏览量加一
     *
     * 同时当前用户在24小时内使用本设备再次访问该物品页面将不再为其浏览量加一
     */
    fun browse(goodsID: Int?, userID: Int?) {
        if (goodsID == null) return

        val isBrowsedIn24h = jsonCacheUtil.getCache(JsonCacheConstantUtil.GOODS_BROWSED + goodsID + userID, Boolean::class.java)
        if (isBrowsedIn24h == null) {
            jsonCacheUtil.saveCache(JsonCacheConstantUtil.GOODS_BROWSED + goodsID + userID, true, 24 * 60 * 60 * 1000L)
            viewModelScope.launch {
                goodsRepository.browseGoods(goodsID)
            }
        }
    }


}