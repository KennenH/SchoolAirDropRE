package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.repository.ChatAllRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InstanceMessageViewModel(private val repository: ChatAllRepository) : ViewModel() {

    class InstanceViewModelFactory(private val repository: ChatAllRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InstanceMessageViewModel::class.java)) {
                return InstanceMessageViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 保存在线接受的消息
     */
    fun saveInstanceMessage(history: ChatHistory) {
        viewModelScope.launch {
            repository.saveHistory(history)
        }
    }
}