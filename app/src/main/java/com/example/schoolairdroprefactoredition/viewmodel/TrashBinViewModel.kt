package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.domain.DomainTrashBin
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.repository.TrashBinRepository
import kotlinx.coroutines.launch


class TrashBinViewModel : ViewModel() {

    private val trashBinRepository by lazy {
        TrashBinRepository.getInstance()
    }

    val trashBinLoadState = MutableLiveData<LoadState>()

    private val mTrashBin = MutableLiveData<DomainTrashBin>()

    fun getTrashBin(token: String): LiveData<DomainTrashBin> {
        viewModelScope.launch {
            trashBinLoadState.value = LoadState.LOADING
            trashBinRepository.getTrashBin(token) { success, response ->
                if (success) {
                    mTrashBin.postValue(response)
                    trashBinLoadState.value = LoadState.SUCCESS
                } else {
                    trashBinLoadState.value = LoadState.ERROR
                }
            }
        }
        return mTrashBin
    }
}