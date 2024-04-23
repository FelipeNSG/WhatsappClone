package com.example.whatsappclone.screeens.loginScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.AuthenticationFirebaseManager
import com.example.whatsappclone.data.FireStoreManager

class LoginScreenViewModel(
    private val fireStore: FireStoreManager,
    private val firebaseAuth: AuthenticationFirebaseManager
) : ViewModel() {


    fun checkNumber(
        numberPhone: String,
        numberPhoneWithLada: String,
        context: Context,
        onCodeSent: (String) -> Unit,
        callBackVerification: (Boolean) -> Unit
    ): Boolean {
        val validNumber: Boolean = try {
            val validationNumberPhone = numberPhone.toString()
            true
        } catch (_: Exception) {
            false
        }
        return if (numberPhone.isNotBlank() && validNumber) {
            onLoginClicked(context, numberPhoneWithLada, onCodeSent, callBackVerification)
            true
        } else {
            false
        }
    }

    private fun onLoginClicked(
        context: Context,
        phoneNumberWithLada: String,
        onCodeSent: (String) -> Unit,
        callBackVerification: (Boolean) -> Unit
    ) {
        firebaseAuth.onLoginClicked(context, phoneNumberWithLada, onCodeSent, callBackVerification)
    }
}

class MyViewModelFactoryLoginScreen(
    private val fireStore: FireStoreManager,
    private val firebaseAuth: AuthenticationFirebaseManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            LoginScreenViewModel(fireStore, firebaseAuth) as T

        } else throw Exception("Error Factory")
    }
}