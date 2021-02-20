package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.GoodsRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class GoodsViewModel : ViewModel() {

    private val goodsDetail = MutableLiveData<GoodsDetailInfo?>()

    private val goodsRepository by lazy {
        GoodsRepository.getInstance()
    }

    fun getGoodsDetailByID(goodsID: Int): LiveData<GoodsDetailInfo?> {
        viewModelScope.launch {
            goodsRepository.getGoodsDetail(goodsID) { success, response ->
                if (success) {
                    goodsDetail.postValue(response)
                } else {
                    goodsDetail.postValue(null)
                }
            }
        }
        return goodsDetail
    }
}