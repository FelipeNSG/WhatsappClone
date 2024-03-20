package com.example.whatsappclone.screeens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager

class LoginScreenViewModel(private val fireStore: FireStoreManager) : ViewModel() {

}

class MyViewModelFactoryLoginScreen (private val fireStore: FireStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            LoginScreenViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }
}