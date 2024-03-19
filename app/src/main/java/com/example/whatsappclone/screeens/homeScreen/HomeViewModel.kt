package com.example.whatsappclone.screeens.homeScreen

import androidx.lifecycle.ViewModel
import com.example.whatsappclone.domain.ChatListDataObject
import com.example.whatsappclone.utils.FireStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

//TODO(MOVE VARIABLES AND FUNCTIONS INSIDE CLASS)

class HomeViewModel(var fireStoreManager: FireStoreManager) : ViewModel() {


    private val scope = CoroutineScope(Dispatchers.IO)
    private var _userList = MutableSharedFlow<List<ChatListDataObject>>()
    val userList: Flow<List<ChatListDataObject>> = _userList

    fun updateFlow() {
        scope.launch {

        }
    }

    fun createContact() {

    }

    fun createChatScreen() {

    }

}
