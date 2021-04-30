package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.domain.DomainUserIWant
import com.example.schoolairdroprefactoredition.repository.SSBRepository

class SSBViewModel : ViewModel() {

    /**
     * 当前获取的在售是第几页
     */
    private val sellingPage = 1

    /**
     * 当前获取的求购是第几页
     */
    private val iwantPage = 1

    private val ssbRepository by lazy {
        SSBRepository.getInstance()
    }

    /**
     * 下架物品
     */
    fun deleteGoods(token: String, goodsID: String): LiveData<Boolean> {
        val deleteGoodsResultLiveData = MutableLiveData<Boolean>()
        ssbRepository.deleteGoods(token, goodsID) {
            deleteGoodsResultLiveData.postValue(it)
        }
        return deleteGoodsResultLiveData
    }

    /**
     * 获取用户在售物品
     */
    fun getSelling(userID: Int): LiveData<DomainSelling?> {
        val sellingLiveData = MutableLiveData<DomainSelling?>()
        ssbRepository.getSelling(userID) {
            sellingLiveData.postValue(it)
        }
        return sellingLiveData
    }

    /**
     * 删除求购
     */
    fun deleteIWant(token: String, iwantID: String): LiveData<Boolean> {
        val deleteIWantResultLiveData = MutableLiveData<Boolean>()
        ssbRepository.deleteIWant(token, iwantID) {
            deleteIWantResultLiveData.postValue(it)
        }
        return deleteIWantResultLiveData
    }

    /**
     * 获取用户求购
     */
    fun getIWant(userID: Int): LiveData<DomainUserIWant?> {
        val iwantLiveData = MutableLiveData<DomainUserIWant?>()
        ssbRepository.getIWant(userID, iwantPage) {
            iwantLiveData.postValue(it)
        }
        return iwantLiveData
    }
}