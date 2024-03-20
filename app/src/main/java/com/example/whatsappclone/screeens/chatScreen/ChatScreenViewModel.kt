package com.example.whatsappclone.screeens.chatScreen

import androidx.lifecycle.ViewModel
import com.example.whatsappclone.utils.FireStoreManager


class ChatScreenViewModel(private val fireStoreManager: FireStoreManager) : ViewModel() {
    fun sendMessage(text:String) {
        lisOfMessage[0] = text
    }

    val lisOfMessage = mutableListOf<String>("Hola")
}

