package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.GoodsRepository

class GoodsViewModel : ViewModel() {

    val mQuoteState = MutableLiveData<LoadState>()

    private val goodsDetail = MutableLiveData<GoodsDetailInfo>()

    private val goodsRepository by lazy {
        GoodsRepository.getInstance()
    }

    fun getGoodsDetailByID(goodsID: Int): LiveData<GoodsDetailInfo> {
        goodsRepository.getGoodsDetail(goodsID) { success, response ->
            if (success) {
                goodsDetail.postValue(response)
                mQuoteState.value = LoadState.SUCCESS
            } else {
                mQuoteState.value = LoadState.ERROR
            }
        }
        return goodsDetail
    }
}