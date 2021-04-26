package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.repository.SellingRepository

class SellingViewModel : ViewModel() {

    private val sellingPage = 0

    private val postsPage = 0

    private val sellingRepository = SellingRepository.getInstance()

    /**
     * 下架物品
     */
    fun deleteGoods(token: String, goodsID: String): LiveData<Boolean> {
        val deleteGoodsResultLiveData = MutableLiveData<Boolean>()
        sellingRepository.deleteGoods(token, goodsID) {
            deleteGoodsResultLiveData.postValue(it)
        }
        return deleteGoodsResultLiveData
    }

    /**
     * 获取用户在售物品
     */
    fun getSelling(userID: Int): LiveData<DomainSelling?> {
        val sellingLiveData = MutableLiveData<DomainSelling?>()
        sellingRepository.getSelling(userID) {
            sellingLiveData.postValue(it)
        }
        return sellingLiveData
    }
}