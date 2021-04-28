package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.IWantCache
import com.example.schoolairdroprefactoredition.repository.IWantRepository
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.utils.AppConfig
import kotlinx.coroutines.launch

class IWantViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class IWantViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(IWantViewModel::class.java)) {
                return IWantViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 页码从1开始，第0页和第1页是一样的，会导致一开始重复拿到一样的两页
     */
    private var page = 1

    private var longitude = AppConfig.DEBUG_LONGITUDE

    private var latitude = AppConfig.DEBUG_LATITUDE

    private val iWantRepository by lazy {
        IWantRepository.getInstance()
    }

    /**
     * 获取上一次app打开时获取的第一批数据作为placeholder
     */
    fun getIWantCache(): LiveData<DomainIWant?> {
        val iwantCacheLiveData = MutableLiveData<DomainIWant?>()
        viewModelScope.launch {
            iwantCacheLiveData.postValue(DomainIWant(data =
            ArrayList<DomainIWant.Data>().also {
                val cacheList = databaseRepository.getIWantCache()
                for (cache in cacheList) {
                    it.add(DomainIWant.Data(
                            cache.iwant_id,
                            cache.iwant_card_color,
                            cache.iwant_content,
                            cache.iwant_images,
                            DomainIWant.Data.Seller(
                                    user_avatar = cache.user_avatar,
                                    user_id = cache.user_id,
                                    user_name = cache.user_name),
                            cache.iwant_tag))
                }
            }))
        }
        return iwantCacheLiveData
    }

    /**
     * 打开app首次获取附近求购或者刷新时
     */
    fun getNearByIWant(longitude: Double?, latitude: Double?): LiveData<DomainIWant?> {
        val iWantLiveData = MutableLiveData<DomainIWant?>()
        viewModelScope.launch {
            page = 1
            this@IWantViewModel.latitude = latitude ?: AppConfig.DEBUG_LATITUDE
            this@IWantViewModel.longitude = longitude ?: AppConfig.DEBUG_LONGITUDE
            iWantRepository.getNearByIWant(
                    page,
                    this@IWantViewModel.longitude,
                    this@IWantViewModel.latitude) {
                if (it != null) {
                    viewModelScope.launch {
                        ArrayList<IWantCache>(it.data.size).apply {
                            for (datum in it.data) {
                                add(IWantCache(
                                        datum.iwant_id,
                                        datum.iwant_content,
                                        datum.iwant_images,
                                        datum.iwant_color,
                                        datum.tag,
                                        datum.seller.user_id,
                                        datum.seller.user_name,
                                        datum.seller.user_avatar))
                            }
                            databaseRepository.saveIWantCache(this)
                        }
                    }
                }
                iWantLiveData.postValue(it)
            }
        }
        return iWantLiveData
    }

    /**
     * 后续获取附近求购，继续使用之前保存的定位
     */
    fun getNearByIWant(): LiveData<DomainIWant?> {
        val iWantLiveData = MutableLiveData<DomainIWant?>()
        iWantRepository.getNearByIWant(++page, longitude, latitude) {
            iWantLiveData.postValue(it)
        }
        return iWantLiveData
    }
}