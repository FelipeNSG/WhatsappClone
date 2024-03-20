package com.example.whatsappclone.screeens.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager

class RegisterScreenViewModel(val fireStore: FireStoreManager) : ViewModel() {


}

class MyViewModelFactoryRegisterScreen(private val fireStore: FireStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterScreenViewModel::class.java)) {
            RegisterScreenViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }
}