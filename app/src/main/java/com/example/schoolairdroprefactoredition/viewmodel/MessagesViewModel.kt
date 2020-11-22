package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.domain.HomeMessagesBean
import com.example.schoolairdroprefactoredition.domain.base.LoadState

class MessagesViewModel : ViewModel() {

    val messagesLoadState = MutableLiveData<LoadState>()

    fun getMessagesList() : LiveData<HomeMessagesBean> {
        return MutableLiveData()
    }
}