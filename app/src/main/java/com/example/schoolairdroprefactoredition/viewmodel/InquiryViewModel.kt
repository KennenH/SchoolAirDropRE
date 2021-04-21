package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.repository.InquiryRepository
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import com.example.schoolairdroprefactoredition.utils.AppConfig

class InquiryViewModel : ViewModel() {

    private var page = 0

    private var longitude = 0.0

    private var latitude = 0.0

    private val inquiryRepository by lazy {
        InquiryRepository.getInstance()
    }

    private val inquiryLiveData = MutableLiveData<List<BaseIWantEntity>>()

    fun getInquiry(longitude: Double?, latitude: Double?): LiveData<List<BaseIWantEntity>> {
        page = 0
        this.latitude = latitude ?: AppConfig.DEBUG_LATITUDE
        this.longitude = longitude ?: AppConfig.DEBUG_LONGITUDE
        inquiryRepository.getInquiry(page++) {
            inquiryLiveData.postValue(it)
        }
        return inquiryLiveData
    }

    fun getInquiry(): LiveData<List<BaseIWantEntity>> {
        inquiryRepository.getInquiry(page++) {
            inquiryLiveData.postValue(it)
        }
        return inquiryLiveData
    }
}