package com.example.whatsappclone.screeens.verifyScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.AuthenticationFirebaseManager
import com.example.whatsappclone.data.FireStoreManager

class VerifyScreenViewModel(val numberPhoneUser:String, private val fireStore: FireStoreManager, private val firebaseAuth: AuthenticationFirebaseManager, private val storedVerificationId:String) : ViewModel() {

    fun verifyPhoneNumberWithCode(context: Context, code: String ){
        firebaseAuth.verifyPhoneNumberWithCode(context, storedVerificationId, code)
    }

    fun consulterUser (
        phoneNumberUser:String,
        callBack:(LoginStatedScreen) -> Unit
    ){
        var stated:LoginStatedScreen = LoginStatedScreen.Loading
        callBack(stated)
        fireStore.fetchIfUserExist(phoneNumberUser){
                consulterStated ->
            stated = when(consulterStated){
                FireStoreManager.FireStoreManagerUserConsultState.Error -> {
                    LoginStatedScreen.ErrorConnexion
                }

                FireStoreManager.FireStoreManagerUserConsultState.Loading -> {
                    LoginStatedScreen.Loading
                }

                FireStoreManager.FireStoreManagerUserConsultState.UserNotFound-> {
                    LoginStatedScreen.IncorrectNumber
                }

                FireStoreManager.FireStoreManagerUserConsultState.UserFound -> {
                    LoginStatedScreen.CorrectNumber
                }
            }
            callBack(stated)
        }
    }

    sealed class LoginStatedScreen {
        data object Loading : LoginStatedScreen()
        data object CorrectNumber : LoginStatedScreen()
        data object IncorrectNumber : LoginStatedScreen()
        data object ErrorConnexion : LoginStatedScreen()
    }

}

class MyViewModelFactoryVerifyScreen (private val numberPhoneUser:String, private val fireStore: FireStoreManager, private val firebaseAuth: AuthenticationFirebaseManager, private val storedVerificationId:String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(VerifyScreenViewModel::class.java)) {
            VerifyScreenViewModel(numberPhoneUser,fireStore, firebaseAuth, storedVerificationId) as T

        } else throw Exception("Error Factory")
    }
}