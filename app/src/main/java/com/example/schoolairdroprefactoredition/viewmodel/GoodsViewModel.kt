package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.GoodsRepository

class GoodsViewModel : ViewModel() {

    private val goodsRepository by lazy {
        GoodsRepository.getInstance()
    }

    val mQuoteState = MutableLiveData<LoadState>()

    private val mQuoteResult = MutableLiveData<Boolean>()

    fun quoteRequest(token: String,
                     goodsID: String,
                     quotePrice: String) : LiveData<Boolean> {
        mQuoteState.value = LoadState.LOADING
        goodsRepository.quoteItem(token,
                goodsID,
                quotePrice) { success, response ->
            if (success && response != null && response.isSuccess) {
                mQuoteState.value = LoadState.SUCCESS
            } else {
                mQuoteState.value = LoadState.ERROR
            }
        }
        return mQuoteResult
    }
}