package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InstanceMessageViewModel(private val repository: DatabaseRepository) : ViewModel() {

    class InstanceViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InstanceMessageViewModel::class.java)) {
                return InstanceMessageViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    /**
     * 保存接收到的消息
     *
     * repository层会将该会话display置1
     */
    fun saveReceivedMessage(history: ChatHistory) {
        viewModelScope.launch {
            repository.saveHistory(history, true)
        }
    }
}