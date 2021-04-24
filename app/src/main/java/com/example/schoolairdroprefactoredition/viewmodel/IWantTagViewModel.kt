package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.repository.IWantTagRepository

class IWantTagViewModel : ViewModel() {

    private val iWantTagRepository by lazy {
        IWantTagRepository.getInstance()
    }

    /**
     * 获取所有求购标签
     */
    fun getIWantTags(): MutableLiveData<DomainIWantTags?> {
        val tagsLiveData = MutableLiveData<DomainIWantTags?>()
        iWantTagRepository.getIWantTags {
            tagsLiveData.postValue(it)
        }
        return tagsLiveData
    }

}