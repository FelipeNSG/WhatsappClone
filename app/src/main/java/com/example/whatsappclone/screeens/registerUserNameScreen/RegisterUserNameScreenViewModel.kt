package com.example.whatsappclone.screeens.registerUserNameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.AuthenticationFirebaseManager
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.UserAccount

class RegisterUserNameScreenViewModel(
    val userNumberPhone: String,
    private val fireStore: FireStoreManager,
    private val firebaseAuth: AuthenticationFirebaseManager
) : ViewModel() {

    fun createUser(userAlias: String, onSuccess: () -> Unit) {
        val userId = firebaseAuth.getUserId
        userId?.let {
            val newUser = UserAccount(
                numberPhone = userNumberPhone.toLong(),
                userName = userAlias,
                userId = userId
            ).also {
                fireStore.createUser(it, onSuccess)
            }
        }
    }
}

class MyViewModelFactoryRegisterUserNameScreenViewModel(
    private val userNumberPhone: String,
    private val fireStore: FireStoreManager,
    private val firebaseAuth: AuthenticationFirebaseManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterUserNameScreenViewModel::class.java)) {
            RegisterUserNameScreenViewModel(userNumberPhone, fireStore, firebaseAuth) as T

        } else throw Exception("Error Factory")
    }
}