package com.example.whatsappclone.screeens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatListDataObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

//TODO(MOVE VARIABLES AND FUNCTIONS INSIDE CLASS)

class HomeViewModel(val fireStore: FireStoreManager) : ViewModel() {

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

class MyViewModelFactory(private val fireStore: FireStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }
}