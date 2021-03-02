package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.GoodsRepository
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


    private val favoriteGoodsLiveData: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 获取物品详细信息
     */
    fun getGoodsDetailByID(goodsID: Int): LiveData<GoodsDetailInfo?> {
        val goodsDetail = MutableLiveData<GoodsDetailInfo?>()
        viewModelScope.launch {
            goodsRepository.getGoodsDetail(goodsID) { success, response ->
                if (success) {
                    response?.let {
                        viewModelScope.launch {
                            it.isFavorite = databaseRepository.isFavorite(goodsID)
                            goodsDetail.postValue(it)
                        }
                    }
                } else {
                    goodsDetail.postValue(null)
                }
            }
        }
        return goodsDetail
    }

    /**
     * 切换物品收藏状态
     *
     * @return
     * true 收藏成功
     * false 取消收藏成功
     */
    fun toggleGoodsFavorite(favorite: Favorite): LiveData<Boolean> {
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