package com.example.whatsappclone.screeens.chatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager


class ChatScreenViewModel(private val fireStoreManager: FireStoreManager) : ViewModel() {
    fun sendMessage(text:String) {
        lisOfMessage[0] = text
    }

    val lisOfMessage = mutableListOf<String>("Hola")
}

class MyViewModelFactoryChatScreen(private val fireStore: FireStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatScreenViewModel::class.java)) {
            ChatScreenViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }
}