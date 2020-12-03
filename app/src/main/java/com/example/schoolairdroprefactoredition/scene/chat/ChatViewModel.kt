package com.example.schoolairdroprefactoredition.scene.chat

import androidx.lifecycle.ViewModel
import com.example.schoolairdroprefactoredition.presenter.impl.ChatImpl

class ChatViewModel : ViewModel() {

    private val chatImpl by lazy {
        ChatImpl.getInstance()
    }


}