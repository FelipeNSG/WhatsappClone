package com.example.whatsappclone.screeens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.dataStore.DataStoreSingleton
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreSingleton.getInstance(application)
    private var getUser: String = ""
    private var getAllowPass: Boolean = false

    init {
        viewModelScope.launch {
            dataStore.getIsEnableToPassToHomeScreen().collect() {
                getAllowPass = it
            }
        }
        viewModelScope.launch {
            dataStore.getUser().collect() {
                getUser = it
            }
        }
    }

    fun getUserId(): String {
        return getUser
    }

    fun getPermissionToPass(): Boolean {
        return getAllowPass
    }

    fun getAllow():Boolean{
        return (getUser.isEmpty() && getAllowPass)  || (getUser.isNotEmpty() && getAllowPass)
    }
}