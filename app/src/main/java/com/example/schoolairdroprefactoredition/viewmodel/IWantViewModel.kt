package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.repository.IWantRepository
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import com.example.schoolairdroprefactoredition.utils.AppConfig

class IWantViewModel : ViewModel() {

    /**
     * 页码从1开始，第0页和第1页是一样的，会导致一开始重复拿到一样的两页
     */
    private var page = 1

    private var longitude = AppConfig.DEBUG_LONGITUDE

    private var latitude = AppConfig.DEBUG_LATITUDE

    private val inquiryRepository by lazy {
        IWantRepository.getInstance()
    }

    private val inquiryLiveData = MutableLiveData<List<BaseIWantEntity>>()

    fun getIWant(longitude: Double?, latitude: Double?): LiveData<List<BaseIWantEntity>> {
        page = 1
        this.latitude = latitude ?: AppConfig.DEBUG_LATITUDE
        this.longitude = longitude ?: AppConfig.DEBUG_LONGITUDE
        inquiryRepository.getIWant(page++) {
            inquiryLiveData.postValue(it)
        }
        return inquiryLiveData
    }

    fun getIWant(): LiveData<List<BaseIWantEntity>> {
        inquiryRepository.getIWant(page++) {
            inquiryLiveData.postValue(it)
        }
        return inquiryLiveData
    }
}