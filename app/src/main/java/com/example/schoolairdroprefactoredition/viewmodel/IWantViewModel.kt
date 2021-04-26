package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.repository.IWantRepository
import com.example.schoolairdroprefactoredition.database.BaseIWantEntity
import com.example.schoolairdroprefactoredition.utils.AppConfig

class IWantViewModel : ViewModel() {

    /**
     * 页码从1开始，第0页和第1页是一样的，会导致一开始重复拿到一样的两页
     */
    private var page = 1

    private var longitude = AppConfig.DEBUG_LONGITUDE

    private var latitude = AppConfig.DEBUG_LATITUDE

    private val iWantRepository by lazy {
        IWantRepository.getInstance()
    }

    private val iWantLiveData = MutableLiveData<List<BaseIWantEntity>>()

    fun getNearByIWant(longitude: Double?, latitude: Double?): LiveData<List<BaseIWantEntity>> {
        page = 1
        this.latitude = latitude ?: AppConfig.DEBUG_LATITUDE
        this.longitude = longitude ?: AppConfig.DEBUG_LONGITUDE
        iWantRepository.getNearByIWant(page++) {
            iWantLiveData.postValue(it)
        }
        return iWantLiveData
    }

    fun getNearByIWant(): LiveData<List<BaseIWantEntity>> {
        iWantRepository.getNearByIWant(page++) {
            iWantLiveData.postValue(it)
        }
        return iWantLiveData
    }
}