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

    private val goodsDetail = MutableLiveData<GoodsDetailInfo?>()

    private val goodsRepository by lazy {
        GoodsRepository.getInstance()
    }

    /**
     * 获取物品详细信息
     */
    fun getGoodsDetailByID(goodsID: Int): LiveData<GoodsDetailInfo?> {
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
     */
    fun toggleGoodsFavorite(goodsID: Int) {
        viewModelScope.launch {
            databaseRepository.isFavorite(goodsID).let {
                if (it) {
                    databaseRepository.removeFavorite(Favorite(goodsID))
                } else {
                    databaseRepository.addFavorite(Favorite(goodsID))
                }
            }
        }
    }
}