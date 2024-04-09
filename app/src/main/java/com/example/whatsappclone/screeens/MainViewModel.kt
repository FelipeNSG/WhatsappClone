package com.example.whatsappclone.screeens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.dataStore.DataStoreSingleton
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreSingleton.getInstance(application)
    var getUser: String? = null

    init {
        viewModelScope.launch {
            dataStore.getUser().collect() {
                getUser = it
            }
        }
    }

    fun setUser(userAccount: String) {
        viewModelScope.launch {
            dataStore.setUser(userAccount)
        }
    }

    fun getUserId(): String? {
        return getUser
    }

}