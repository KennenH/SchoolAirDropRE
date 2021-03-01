package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavoriteViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class FavoriteViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private val favoriteLiveData: MutableLiveData<List<Favorite>> = MutableLiveData()

    fun getFavorites(): LiveData<List<Favorite>> {
        viewModelScope.launch {
            favoriteLiveData.postValue(databaseRepository.getFavorites())
        }
        return favoriteLiveData
    }
}