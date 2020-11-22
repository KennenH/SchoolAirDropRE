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

    private val mCorrupted = MutableLiveData<DomainTrashBin>()

    private val mAccomplished = MutableLiveData<DomainTrashBin>()

    fun getCorrupted(token: String): LiveData<DomainTrashBin> {
        viewModelScope.launch {
            trashBinLoadState.value = LoadState.LOADING
            trashBinRepository.getCorrupted(token) { success, response ->
                if (success) {
                    mCorrupted.postValue(response)
                    trashBinLoadState.value = LoadState.SUCCESS
                } else {
                    trashBinLoadState.value = LoadState.ERROR
                }
            }
        }
        return mCorrupted
    }

    fun getAccomplished(token: String): LiveData<DomainTrashBin> {
        viewModelScope.launch {
            trashBinLoadState.value = LoadState.LOADING
            trashBinRepository.getAccomplished(token) { success, response ->
                if (success) {
                    mAccomplished.postValue(response)
                    trashBinLoadState.value = LoadState.SUCCESS
                } else {
                    trashBinLoadState.value = LoadState.ERROR
                }
            }
        }
        return mAccomplished
    }
}