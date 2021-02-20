package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.DomainPostInfo
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.repository.SellingRepository

class SellingViewModel : ViewModel() {

    private val sellingPage = 0

    private val postsPage = 0

    private val sellingRepository = SellingRepository.getInstance()

    private val deleteGoodsResultLiveData = MutableLiveData<Boolean>()
    private val updateGoodsResulLiveData = MutableLiveData<Boolean>()

    private val sellingLiveData = MutableLiveData<DomainPurchasing?>()
    private val postsLiveData = MutableLiveData<DomainPostInfo?>()

    private val deletePostResultLiveData = MutableLiveData<Boolean>()
    private val updatePostResultLiveData = MutableLiveData<Boolean>()

    fun deleteGoods(token: String, goodsID: String): LiveData<Boolean> {
        sellingRepository.deleteGoods(token, goodsID) {
            deleteGoodsResultLiveData.postValue(it)
        }
        return deleteGoodsResultLiveData
    }

    fun getSelling(userID: Int): LiveData<DomainPurchasing?> {
        sellingRepository.getSelling(userID) {
            sellingLiveData.postValue(it)
        }
        return sellingLiveData
    }
}